import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class BooleanSearchEngine implements SearchEngine {

    protected Map<String, List<PageEntry>> queryList = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] files = pdfsDir.listFiles();
        FileStopwordRead fileStopwordRead = new FileStopwordRead("stop-ru.txt");

        assert files != null;

        for (File pdf : files) {
            var doc = new PdfDocument(new PdfReader(pdf));

            Map<String, Integer> freqs = new HashMap<>();

            for (int i = 1; i < doc.getNumberOfPages(); i++) {
                var page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }

                    word = word.toLowerCase();

                    if (fileStopwordRead.text.contains(word)) {
                        continue;
                    }

                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
            }
            for (Map.Entry<String, Integer> map : freqs.entrySet()) {
                PageEntry pageEntry = new PageEntry(pdf.getName(), doc.getNumberOfPages(), freqs.get(map.getKey()));
                if (!queryList.containsKey(map.getKey())) {
                    List<PageEntry> list = new ArrayList<>();
                    list.add(pageEntry);
                    queryList.put(map.getKey(), list);

                } else {
                    List<PageEntry> list2 = queryList.get(map.getKey());
                    list2.add(pageEntry);
                    queryList.put(map.getKey(), list2);
                }
            }
        }
        for (Map.Entry<String, List<PageEntry>> map : queryList.entrySet()) {
            Collections.sort(queryList.get(map.getKey()));
        }
    }


    public List<PageEntry> searchImpl(String word) {
        List<PageEntry> pageEntries = new ArrayList<>();
        String[] words = word.split(" ");
        for (String word2 : words) {
            if (word2.isEmpty()) {
                continue;
            }
            if (queryList.containsKey(word2)) {
                pageEntries.addAll(queryList.get(word2));
                return pageEntries;
            }
        }
        return pageEntries;
    }


    @Override
    public List<PageEntry> search(String word) {
        return searchImpl(word);
    }
}
