public class Classes {
    // Поле с модификаторами доступа
    public int publicField;
    private int privateField;
    protected int protectedField;
    int defaultField;
    static int staticField;
    final int finalField = 10;

    // Конструктор с различными модификаторами
    public ModifiersExample() {
    }
    private ModifiersExample(int value) {
    }
    protected ModifiersExample(String text) {
    }

    // Методы с модификаторами доступа и другими модификаторами
    public void publicMethod() {
    }
    private void privateMethod() {
    }
    protected void protectedMethod() {
    }
    void defaultMethod() {
    }
    static void staticMethod() {
    }
    final void finalMethod() {
    }
    abstract void abstractMethod();

    // Внутренний статический класс
    static class StaticInnerClass {
    }

    // Внутренний класс
    class InnerClass {
    }

    // Вложенный интерфейс
    interface NestedInterface {
    }

}
