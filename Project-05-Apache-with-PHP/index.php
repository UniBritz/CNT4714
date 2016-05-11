<!--
Name: 				Brian Batinchok
Course: 			CNT 4714 – Spring 2016
Assignment title: 	A Three-Tier Distributed Web-Based Application Using PHP and Apache
Due Date: 			April 25th, 2016 
-->

<!DOCTYPE HTML>
<html>
	<head>
		<title>Project 5 Login</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	
	<header>
		<h1>Project Five Database Client</h1><hr>
	</header>
	
	<body>
		<?php

		session_start();

        //variable declarations for script run
		// 'login' used as a logged in user check
        if(session_id() == ""){
            $_SESSION['username'] = "";
            $_SESSION['password'] = "";
            $_SESSION['login'] = 0;
        }//if
		
		//'main' used here as indicator for php to print at main page
		//sets url to localhost and name to project5
        $_SESSION['main'] = 1;
        $database_url = "localhost";
        $database = "project5";
        $i = 0;

		//logs user out if logout clicked; location of statement prevents bad html placement
        if(isset($_POST['logout'])){
            $_SESSION['login'] = 0;
        }//if logout

        //checks the login status of the user
        if(isset($_POST['login_button'])){
            $_SESSION['login'] = 1;
            $_SESSION['username'] = $_POST["username"];
            $_SESSION['password'] = $_POST["password"];
        }//if logged on

		//prints html form to user if user is not logged in
        if($_SESSION['login'] == 0){
            echo "<div id='login_form'>
					<form id=\"login\" method=\"post\" action=\"index.php\">
						Username <input id=\"username\" name=\"username\" type=\"text\"><br>
						Password <input id=\"password\" name=\"password\" type=\"password\"><br>
						<input type=\"submit\" name=\"login_button\" id=\"login_button\" value=\"Login\">
					</form>
                </div>

                <div id=\"info\">
					<h2>Welcome to the Database Client</h2>
					<p>Run SQL queries and update statements on Project 5 Database</p>
					<h2>Database</h2>
					<p>Connecting to MySQL Database</p>
					<h2>Features</h2>
					
					<ul>
						<li>User Authentication</li>
						<li>Select Query</li>
						<li>Update Query</li>
						<li>Business Logic Implementation</li>
						<li>Result Page</li>
						<li>Error Checking</li>
					</ul>
					
				<h2>User Login</h2>
					<p>Use the following on the left</p>
					<ul>
						<li><b>Username:</b>Admin</li>
						<li><b>Password:</b>Pass</li>
					</ul>
			    </div>";
        }//if not logged in

		//creates db connections and selects appropriate database
        if($_SESSION['login'] == 1){
            $database_connection = mysqli_connect($database_url, $_SESSION['username'], $_SESSION['password'])
				or die("unable to connect");
            $selected_database = mysqli_select_db($database_connection, $database)
				or die("Unable to select database<br>");

            echo "<div id=\"welcome_box\">
					<p>Welcome Back</p>
					<p>" . $_SESSION['username'] . "</p>
						<form id=\"back\" method=\"post\" action=\"index.php\">
							<input type=\"submit\" name=\"logout\" id=\"logout\" value=\"Logout\">
						</form>
                </div>";

			//prints if Submit Query is pressed
			//retrieves the user query results
            if(isset($_POST['submit_query'])){
                $_SESSION['main'] = 0; //indicates the user is no longer on main page
                $query = $_POST['user_query'];
                echo "<div id=\"results\">";
				
				//pulls metadata and columns
				//creates the table with header, updates table from results
                if($result = mysqli_query($database_connection, $query)){
                    $metadata = mysqli_fetch_fields($result);
                    $num_columns = mysqli_num_fields($result);

                    echo "<h2>Query Results</h2>";
                    echo "<table>";
                    echo "<tr>";
                    for($i = 0; $i < $num_columns; $i++){
                        echo "<th>" . $metadata[$i]->name . "</th>";
                    }//for
                    echo "</tr>";

                    while ($row = mysqli_fetch_row($result)){
                        echo "<tr>";
                        for ($i = 0; $i < $num_columns; $i++){
                            echo "<td>" . $row[$i] . "</td>";
                        }//for
                        echo "</tr>";
                    }//while
                    echo "</table>";
                }//if for query results

                else{
                    echo 
					"<p>SQL Exception occurred while interacting with Project 5 database.</p>
                    <p>The Error message was:<br>" . mysqli_error($database_connection) . "</p>
                    <p>Please Try again later.</p>";
                    }//else

                echo "<a href=\"index.php\">Return to Main</a>";
                echo "</div>";
            }//submit query

			//gets the user query result
            if(isset($_POST['submit_update'])){
                $_SESSION['main'] = 0; //user no longer in main
                $query = $_POST['user_query'];

                echo "<div id=\"results\">";

                //Gets the result of the query
				//jsalert for business logic
				//gets required fields, metadata, prints results to table
                if(mysqli_query($database_connection, $query)){
                    echo '<SCRIPT LANGUAGE="JavaScript1.1">window.alert(
			        "ALERT: SUPPLIER STATUS HAS CHANGED DUE TO BUSINESS LOGIC. DISPLAYING UPDATED SUPPLIER TABLE!");
			        </SCRIPT> ';

                    $business_suppliers = mysqli_query($database_connection,"select distinct snum from shipments where quantity >= 100");

                    while ($business_rows = mysqli_fetch_row($business_suppliers)){
                        mysqli_query($database_connection,"update suppliers set status = status + 5 where snum = '" . $business_rows[$i] . "'");
                    }//while

                    //query results header
                    echo "<h2>Insert Query Results</h2>";

                    $insert_query = "SELECT * FROM suppliers";
                    $result = mysqli_query($database_connection, $insert_query);
                    $metadata = mysqli_fetch_fields($result);
                    $num_columns = mysqli_num_fields($result);

                    echo "<table>";
                    echo "<tr>";
                    for($i = 0; $i < $num_columns; $i++){
                        echo "<th>" . $metadata[$i]->name . "</th>";
                    }//for
					
                    echo "</tr>";

                    while ($row = mysqli_fetch_row($result)){
                        echo "<tr>";
                        for ($i = 0; $i < $num_columns; $i++){
                            echo  "<td>" . $row[$i] . "</td>";
                        }//for
                        echo "</tr>";
                    }//while

                    echo "</table>";
                }//if for query results
				
                else{
                    echo "<p>SQL Exception occurredinteracting withProject 5 database.</p>
                        <p>The Error message was:<br>" . mysqli_error($database_connection) . "</p>
                        <p>Please Try again later.</p>";
                }//else

                echo "<a href=\"index.php\">Return to Main Page</a>";
                echo "</div>";
            }//submit update

            //prints if the user is on main page
            if ($_SESSION['main'] == 1){
                echo "<div id=\"query_box\">
						<p>Enter Query</p>
						<p>Please enter a valid SQL query or update statement.
							You may also just press \"Submit Query\" to run a
							default query against the database.</p>
                    <form method=\"post\" action=\"index.php\">
						<textarea rows=\"15\" cols=\"60\" id=\"user_query\" name=\"user_query\">select * from suppliers</textarea>
						<br>
						<input type=\"submit\" value=\"Submit Query\" id=\"submit_query\" name=\"submit_query\"/>
						<input type=\"submit\" value=\"Submit Update\" id=\"submit_update\"name=\"submit_update\"/>
						<input type=\"submit\" value=\"Reset Window\" id=\"reset\" name=\"reset\"/>
                    </form>
                    </div>";
            }//main

            mysqli_close($database_connection);
        }//login
        ?>
		
    <footer>
        <hr>
        <p>&copy; CNT 4714 PHP Database Client</p>
    </footer>
	</body>
</html>