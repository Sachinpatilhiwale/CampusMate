public class Student {
    public String id;
    public String name;
    public String roll;
    public String status; // Add this field

    public Student() {}

    public Student(String id, String name, String roll, String status){
        this.id = id;
        this.name = name;
        this.roll = roll;
        this.status = status; // "Present" or "Absent"
    }
}