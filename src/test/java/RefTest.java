import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class RefTest {

    public static void main(String[] args) throws InterruptedException {
//        gcTest();
//        twoRefsTest();
//        arrTest();
        listTest();
    }

    private static void gcTest() throws InterruptedException {
        ReferenceQueue<Object> queue = new ReferenceQueue<>();

        // Create an Object in the heap
        // Create a strong reference to the Object
        Object referent = new Object();

        // Create a weak reference to the Object
        Reference<Object> reference = new WeakRefWithName<>(referent, queue);

        // Clear the strong reference to the Object
        // This leaves only the weak reference to the Object
        referent = null;

        // Call the garbage collector, which will realize that there is only a weak reference to the Object and reclaim it
        // The weak reference will be added to the queue because the reachability of the Object changed from strong to weak
        System.gc();

        // Get the reference whose referent's reachability has changed
        WeakRefWithName removed = (WeakRefWithName) queue.remove();
        print(removed.referentName + " finalized");
    }

    private static void twoRefsTest() {
        // Create obj in heap
        // Create 2 refs to obj
        Obj obj1 = new Obj("foo");
        Obj obj2 = obj1;
        print(obj1);
        print(obj2);

        // Using 1 ref, change obj name
        // Both ref's names will change cuz they're referencing the same obj
        obj2.name = "bar";
        print(obj1);
        print(obj2);


    }

    private static void arrTest() {
        Obj[] arr = new Obj[10];

        arr[0] = new Obj("foo");
        print(arr);

        Obj obj = arr[0];
        print(obj);

        obj.name = "bar";
        print(obj);
        print(arr);
    }

    private static void listTest() {
        List<Obj> list = new ArrayList<>(10);

        list.add(new Obj("foo"));
        print(list);

        Obj obj = list.get(0);
        print(obj);

        obj.name = "bar";
        print(obj);
        print(list);
    }

    private static void print(Object object) {
        if (object instanceof Object[])
            object = Arrays.toString((Object[]) object);
        System.out.println(object);
    }

    private static class Obj {
        private String name;

        public Obj(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Obj.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .toString();
        }
    }

    private static class WeakRefWithName<T> extends WeakReference<T> {

        private final String referentName;

        private WeakRefWithName(T referent, ReferenceQueue<? super T> q) {
            super(referent, q);
            referentName = referent.toString();
        }

    }

}
