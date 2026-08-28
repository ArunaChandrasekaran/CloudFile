<!DOCTYPE html>
<html>
<head>
    <title>CSS Operations using DOM</title>

    <style>
        body {
            font-family: Arial;
            text-align: center;
            padding: 30px;
        }

        #box {
            width: 300px;
            padding: 30px;
            margin: 20px auto;
            background-color: lightblue;
            font-size: 20px;
        }

        button {
            padding: 10px 15px;
            margin: 5px;
        }
    </style>
</head>

<body>

    <h2>MENU - CSS OPERATIONS</h2>

    <div id="box">
        This is my DOM element
    </div>

    <button onclick="changeColor()">1. Change Color</button>
    <button onclick="increaseFont()">2. Increase Font Size</button>
    <button onclick="hideElement()">3. Hide Element</button>
    <button onclick="exitProgram()">4. Exit</button>


    <script>

        // Change color
        function changeColor() {

            let box = document.getElementById("box");

            box.style.backgroundColor = "pink";
            box.style.color = "red";
        }


        // Increase font size
        function increaseFont() {

            let box = document.getElementById("box");

            box.style.fontSize = "30px";
        }


        // Hide element
        function hideElement() {

            let box = document.getElementById("box");

            box.style.display = "none";
        }


        // Exit
        function exitProgram() {

            document.body.innerHTML = "<h2>Program Exited</h2>";
        }

    </script>

</body>
</html>
