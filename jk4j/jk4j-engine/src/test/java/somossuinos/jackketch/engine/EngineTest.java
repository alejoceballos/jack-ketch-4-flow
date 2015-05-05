package somossuinos.jackketch.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

class MyTest {
    public String doSomething(Map<String, Object> map) {
        return map.toString();
    }
}

public class EngineTest {

    @Test
    public void test() {
        try {
            final Class clz = Class.forName("somossuinos.jackkeTtch.engine.MyTest");
            final Method m = clz.getMethod("doSomething", Map.class);

            Object result = null;

            if (String.class.equals(m.getReturnType())) {
                Object obj = clz.newInstance();
                result = m.invoke(obj, new HashMap<>());

            } else {
                throw new RuntimeException("Shit");
            }

            return;

        } catch(ClassNotFoundException e) {

        } catch(InstantiationException e) {

        } catch(IllegalAccessException e) {

        } catch(NoSuchMethodException e) {

        } catch(InvocationTargetException e) {

        }
    }

}
