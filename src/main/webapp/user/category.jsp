<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Category</title>
</head>
<body>
<%@include file="../includes/navbar.jsp" %>

	<div class="container my-3">
		<form id="addForm">
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Category</th>
					<th scope="col">Type</th>
					<th scope="col">Add</th>
				</tr>
			</thead>
			<tbody>
				<tr>
						<td><input type="text" class="form-control" name="category" 
							required></td>
						<td><select name="type" id="typeSelect" class="form-control" required>
						<option value="1">Expense</option>
						<option value="2">Income</option>
						</select>
						</td>
						<td>
							<button type="submit" class="btn btn-sm btn-success">Add</button>
						</td>
				</tr>
			</tbody>
		</table>
		</form>
		<br>
		<table class="table table-stripped">
			<thead>
				<tr>
					<th scope="col">Category</th>
					<th scope="col">Type</th>
					<th scope="col">Date</th>
					<th scope="col">Update</th>
				</tr>
			</thead>
			<tbody id="updateTableBody"></tbody>
		</table>
	</div>
	
	<script type="text/javascript">
	const userId = sessionStorage.getItem('userId');
	const roleId = sessionStorage.getItem('userRoleId');
        document.getElementById("addForm").addEventListener("submit", function(event){
        event.preventDefault();
        
        if(!validateForm(this)){
            return;
        }
        
        fetch("../category/add?userId="+userId,{
            method:"POST",
            body: new FormData(this)
        }).then(response=>{
            if(response.ok){
                alert("Category added successfully.");
                this.reset();
                populateTable();
            }
            else if(response.status === 409){
	            alert('Given Category is already exist, Try with another name.');
	            return;
	        }
            else{
                throw new Error("Error in adding category.");
            }
            
        }).catch(error => {
                console.error('Error adding expense:', error);
                alert("Failed to add category. Please try again.");
        })
        
        });
        
        function validateForm(form){
            let category = form.querySelector('input[name="category"]').value;
            let select = form.querySelector('select[name="type"]').value;
        
            if(!category){
                alert("Category cannot be empty.");
                return false;
            }
        
            if(!select){
                alert("Category cannot be empty.");
                return false;
            }
        
            return true;
        }

        function updateExpense(categoryId) {

            const row = document.getElementById('row_' + categoryId);
            const category = row.querySelector('input[name="category"]').value;
    
            fetch("../category/update?userId="+userId+"&categoryId="+categoryId+"&category="+category)
            .then(response => {
                if (response.ok) {
                    alert('Category updated successfully');
                    populateTable();
                } 
                else if(response.status === 400){
    	            throw new Error('Wrong Parameter type.');
    	        }
    	        else if(response.status === 409){
    	            alert('Given Category is already exist, Try with another name.');
    	            return;
    	        }
    	        else {
                    throw new Error('Failed to update expense');
                }
            })
            .catch(error => {
                console.error('Error updating expense:', error);
                alert('Failed to update expense. Please try again.');
            });
        }
        
        function populateTable() {
            let url = "../category/get?userId=" + userId;
            fetch(url)
                .then(response => response.json())
                .then(categories => {
                    let updateFormBody = document.getElementById("updateTableBody");
                    updateFormBody.innerHTML = "";

                    categories.forEach(category => {
                        let rowTag = document.createElement("tr");
                        rowTag.id = 'row_' + category.categoryId;

                        if (roleId == category.roleId) {
                            rowTag.innerHTML =
                                "<td><input name='category' class='form-control' value='" + category.category + "' required></td>" +
                                "<td>" + category.transactionType + "</td>" +
                                "<td>"+category.addDate+"</td>"+
                                "<td> <button type='button' class='btn btn-sm btn-success' onclick='updateExpense(" + category.categoryId + ")'>Update</button> </td>";
                        } else {
                            rowTag.innerHTML =
                                "<td>" + category.category + "</td>" +
                                "<td>" + category.transactionType + "</td><td>Not visible</td><td>Not Accessible</td>";
                        }

                        updateFormBody.appendChild(rowTag);
                    });
                })
                .catch(error => console.error('Error fetching category data:', error));
        }

        window.addEventListener('load', populateTable);
	</script>

</body>
</html>