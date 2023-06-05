import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 10314;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostName());

                // handling the client request in a separate thread
                Thread thread = new Thread(() -> handleRequest(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void handleRequest(Socket socket) {
        try (
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            String methodName = ois.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
            Object[] parameters = (Object[]) ois.readObject();

            Method method = Server.class.getMethod(methodName, parameterTypes);
            Object result = method.invoke(null, parameters);

            oos.writeObject(result);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // Extra cred: language-portable wire format
    private static void sendRequest(ObjectOutputStream oos, String methodName, Class<?>[] parameterTypes,
            Object[] parameters) throws IOException {
        oos.writeUTF(methodName);
        oos.writeObject(parameterTypes);
        oos.writeObject(parameters);
    }

    // Extra cred: add object instances
    public int multiply(int lhs, int rhs) {
        return lhs * rhs;
    }

    // Extra cred: generalize the remoting
    public static Object invokeMethod(String className, String methodName, Class<?>[] parameterTypes,
            Object[] parameters)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName, parameterTypes);
        return method.invoke(null, parameters);
    }

    // Do not modify any code below this line
    // --------------------------------------
    public static String echo(String message) {
        return "You said " + message + "!";
    }

    public static int add(int lhs, int rhs) {
        return lhs + rhs;
    }

    public static int divide(int num, int denom) {
        if (denom == 0)
            throw new ArithmeticException();

        return num / denom;
    }
}
