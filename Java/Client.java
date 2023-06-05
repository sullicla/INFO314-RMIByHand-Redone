import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

    /**
     * This method name and parameters must remain as-is
     */
    public static int add(int lhs, int rhs) {
        try (Socket socket = new Socket("localhost", PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("add");
            out.writeObject(lhs);
            out.writeObject(rhs);
            return (int) in.readObject();
        } catch (ConnectException e) {
            System.out.println("Server is not running. Start the server.");
            return -1;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method name and parameters must remain as-is
     */
    public static int divide(int num, int denom) {
        try (Socket socket = new Socket("localhost", PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("divide");
            out.writeObject(num);
            out.writeObject(denom);
            return (int) in.readObject();
        } catch (ConnectException e) {
            System.out.println("Server is not running. Start the server.");
            return -1;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method name and parameters must remain as-is
     */
    public static String echo(String message) {
        try (Socket socket = new Socket("localhost", PORT)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("echo");
            out.writeObject(message);
            return (String) in.readObject();
        } catch (ConnectException e) {
            System.out.println("Server is not running. Please start the server.");
            return "";
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Do not modify any code below this line
    // --------------------------------------
    String server = "localhost";
    public static final int PORT = 10314;

    public static void main(String... args) {
        // All of the code below this line must be uncommented
        // to be successfully graded.
        System.out.print("Testing... ");

        if (add(2, 4) == 6)
            System.out.print(".");
        else
            System.out.print("X");

        try {
            divide(1, 0);
            System.out.print("X");
        } catch (ArithmeticException x) {
            System.out.print(".");
        }

        if (echo("Hello").equals("You said Hello!"))
            System.out.print(".");
        else
            System.out.print("X");

        System.out.println(" Finished");
    }
}