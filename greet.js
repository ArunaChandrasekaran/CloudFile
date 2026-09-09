
class Person {
  
    constructor(name, age) {
        
        this.name = name;


        this.age = age;
    }

   
    greet() {
        console.log(`Hello, my name is ${this.name} and I am ${this.age} years old`);
    }
}

const person = new Person("Aruna", 25);
person.greet();
