import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class Mtest {

    void f(int a) {

    }

    public Mtest(int b) {

    }

    public Mtest() {
        Constructor<?>[] ctors = this.getClass().getConstructors();
        for (Constructor<?> ctor : ctors) {
            if (ctor.getParameterCount() == 1) {
                System.out.println(ctor.getParameterTypes()[0].equals(int.class));
            }
        }
    }

    public static void main(String[] args) {
        new Mtest();
    }
}
