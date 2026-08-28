<!DOCTYPE html>
<html>
<head>
    <title>DOM Menu Program</title>

    <style>
        body {
            font-family: Arial;
            text-align: center;
            padding: 30px;
        }

        .box {
            width: 400px;
            margin: auto;
            padding: 20px;
            border: 2px solid black;
        }

        button {
            margin: 5px;
            padding: 10px 15px;
            cursor: pointer;
        }

        input {
            padding: 8px;
            margin: 5px;
        }
    </style>
</head>

<body>

    <div class="box" id="box">

        <h2>DOM Menu Program</h2>

        <p id="text">Hello! Welcome to DOM Practice</p>

        <input type="number" id="num1" placeholder="Enter first number">
        <input type="number" id="num2" placeholder="Enter second number">

        <br><br>

        <button onclick="readText()">1. Read Text</button>
        <button onclick="addNumbers()">2. Add Numbers</button>
        <button onclick="changeCSS()">3. Change CSS</button>
        <button onclick="exitProgram()">4. Exit</button>

        <h3 id="result"></h3>

    </div>


    <script>

        // 1. Read text from DOM
        function readText() {

            let text = document.getElementById("text");

            document.getElementById("result").innerText =
                text.innerText;
        }


        // 2. Add two numbers from DOM
        function addNumbers() {

            let num1 = Number(document.getElementById("num1").value);
            let num2 = Number(document.getElementById("num2").value);

            let sum = num1 + num2;

            document.getElementById("result").innerText =
                "Sum = " + sum;
        }


        // 3. Change CSS using DOM
        function changeCSS() {

            let box = document.getElementById("box");

            box.style.backgroundColor = "lightblue";
            box.style.color = "darkblue";
            box.style.border = "3px solid blue";

            document.getElementById("result").innerText =
                "CSS Changed Successfully!";
        }


        // 4. Exit
        function exitProgram() {

            document.getElementById("box").innerHTML =
                "<h2>Program Exited</h2>";

        }

    </script>

</body>
</html>
