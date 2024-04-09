package hard_8;

class Worker extends Student {
    private double salary;

    public Worker(String name, int age, double salary) {
        super(name, age); // Вызов конструктора родительского класса
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}