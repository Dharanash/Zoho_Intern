<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>

<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<meta charset="ISO-8859-1">
<title>Login</title>
<%@include file="../includes/head.jsp"%>
</head>
<body>
	
	
	<a href="../user/showregister" class="btn btn-success"
			style="position: absolute; top: 20px; right: 20px;">Go to Register</a>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">User Login</div>
			<div class="card-body">
				<form id="loginForm">
						<div class="form-group">
							<label>Email address</label> <input type="email" name="email"
								class="form-control" placeholder="Enter email">
						</div>
						<div class="form-group">
							<label>Password</label> <input type="password" name="password"
								class="form-control" placeholder="Enter Password">
						</div>
						<div class="text-center">
							<button type="submit" class="btn btn-primary">Login</button>
						</div>
					</form>
			</div>
		</div>
	</div>
	
	
	<script type="text/javascript">
	
	document.getElementById('loginForm').addEventListener('submit', function(event) {
		   event.preventDefault(); 
		  
		   
		   fetch("../user/login", {
		       method: 'POST',
		       body: new FormData(this)
		   })
		   .then(response => {
		       if (response.ok) {
		         
		         
		         const token = response.headers.get('Authorization');
		         document.cookie = "jwt=" + token + "; path=/;";
		         return response.json();
		       } 
		       else if(response.status === 400){
		           throw new Error('Invalid Email/ Password.');
		       }
		       else{
		          throw new Error('Failed to Login');
		       }
		   })
		   .then(data=>{
		      sessionStorage.setItem('userId', data.userId);
		      sessionStorage.setItem('userName', data.name);
		      sessionStorage.setItem('userRoleId', data.roleId);
		      
		      if(data.roleId==1)
		      {
		    	  window.location.href = "../admin/showhome";
		      }
		      else if(data.roleId==2)
		      {
		    	  window.location.href = "../guser/showhome";
		      }

		   })
		   .catch(error => {
		       console.error('Error adding expense:', error);
		       alert(error);
		   });
		});
	
	
	</script>
</body>
</html>