<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Users</title>
</head>
<body>
<%@include file="../includes/navbar.jsp"%>

	<div class="container my-3">
	 <div class="d-flex py-3">
        <h3 id="totalUsers">Total Users Count:</h3>
    </div>
	<table class="table table-striped">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Email</th>
					<th scope="col">Phone Number</th>
				</tr>
			</thead>
			<tbody id="userTableBody">
				
			</tbody>
		</table>
	</div>
	
	<script type="text/javascript">
	var userId = sessionStorage.getItem('userId');
	window.addEventListener('load', populateTable);
	function populateTable(){
        let url="../admin/getusers?userId="+userId;
        fetch(url)
        .then(response=> response.json())
        .then(users =>{
            let userTableBody= document.getElementById("userTableBody");
            userTableBody.innerHTML="";
            
            let totalUserCount = users.length;
            document.getElementById("totalUsers").textContent = "Total Users Count: " + totalUserCount;

            users.forEach(user=>{
                let rowTag = document.createElement("tr");
                    rowTag.innerHTML=
                    "<td>"+user.name+"</td>" +
                    "<td>"+user.email+"</td>"+
                    "<td>"+user.phoneNumber+"</td>";

                    userTableBody.appendChild(rowTag);
            });
        })
        .catch(error => console.error('Error fetching category data:', error));
    }
	</script>
	
</body>
</html>