/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CollectionHw;

/**
 *
 * @author aruna
 */
class Student {
    private int rollNo;
    private String name;
    private int age;
    private String course;
    private int marks;

    public Student(int rollNo, String name, int age, String course, int marks) 
    {
        this.rollNo = rollNo;
        this.name = name;
        this.age = age;
        this.course = course;
        this.marks = marks;
    }
        
        public int getRollNo() 
        {
        return rollNo;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Roll No : " + rollNo +
                "\nName     : " + name +
                "\nAge      : " + age +
                "\nCourse   : " + course +
                "\nMarks    : " + marks;    }
}

    
