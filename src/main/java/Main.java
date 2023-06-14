import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {

        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs/"));
        String value;


        try (ServerSocket serverSocket = new ServerSocket(8989);) {

            System.out.println("Ожидание подключения...");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {

                    out.println("Введите слово для поиска");

                    value = in.readLine();

                    out.println(TransformationToJson.transToJson(engine.search(value)));

                    System.out.println("Результат поиска отправлен. Ожидание нового подключения...");

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
