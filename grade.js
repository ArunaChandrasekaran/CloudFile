function calculateGrade(marks) {

    if (marks >= 90) {
        return "A+";
    }
    else if (marks >= 75) {
        return "A";
    }
    else if (marks >= 60) {
        return "B";
    }
    else if (marks >= 50) {
        return "C";
    }
    else {
        return "Fail";
    }
}


console.log(calculateGrade(95));  
console.log(calculateGrade(82));  
console.log(calculateGrade(65));  
console.log(calculateGrade(55));  
console.log(calculateGrade(40));  
