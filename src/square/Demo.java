package square;

public class Demo {
  public static void main(String[] args) {
    System.loadLibrary("square");
    System.out.println("square(4)=" + Square.square(4));
  }
}
