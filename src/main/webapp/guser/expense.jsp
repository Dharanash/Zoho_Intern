<%@page import="com.expensecalculator.dto.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Expense</title>
<!-- Include any necessary CSS or JavaScript libraries -->
</head>
<body>
<%@include file="../includes/navbar.jsp" %>
	
	<!-- Modal -->
<div class="modal fade" id="updateExpenseModal" tabindex="-1" aria-labelledby="updateExpenseModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateExpenseModalLabel">Update Expense</h5>
        <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="updateExpenseForm">
          <input type="hidden" class="form-control" name="transactionId" id="expenseIdInput">
          <input type="hidden" class="form-control" name="userId" id="userIdInput">

          <label>Amount:</label>
          <input type="number" class="form-control" name="amount" id="amountInput" required>

          <label>Note:</label>
          <input type="text" class="form-control" name="note" id="noteInput" required>
          
          <label>Category:</label>
          <select name="categoryId" class="form-control" id="updateCategorySelect" required></select>

          <label>Date:</label>
          <input type="date" class="form-control" name="date"  id="dateInput" required>

          <label>Time:</label>
          <input type="time" class="form-control" name="time" id="timeInput" required>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="submitUpdateExpenseForm()">Save changes</button>
      </div>
    </div>
  </div>
</div>

	<div class="container my-3">
		<form id="addExpenseForm">
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Expense Amount</th>
					<th scope="col">Note</th>
					<th scope="col">Category</th>
				</tr>
			</thead>
			<tbody>
				<tr>
						<td><input type="number" class="form-control" name="amount" 
							min="1" step="any" required></td>
						<td><input type="text" class="form-control" name="note" 
							required></td>
						<td><select name="categoryId" id="addCategorySelect" class="form-control" required></select>
						<input type="text" class="form-control" name="category" id="addCategoryInput" placeholder="Custom Category" >
						</td>
						<td>
							<button type="submit" class="btn btn-sm btn-success">Add</button>
						</td>
				</tr>
			</tbody>
		</table>
		</form>
		<div class="d-flex py-3">
			<h3 id="totalExpense">Total Expense:</h3>
		
		</div>
		<br>
		<table class="table table-striped">
			<thead>
				<tr>
					<th scope="col">Amount</th>
					<th scope="col">Note</th>
					<th scope="col">Category</th>
					<th scope="col">Date</th>
					<th scope="col">Time</th>
					<th scope="col">Update</th>
					<th scope="col">Remove</th>
				</tr>
			</thead>
			<tbody id="expenseTableBody"></tbody>
		</table>
	</div>

	<script>
	var transactionTypeId = 1;
	var userId = sessionStorage.getItem('userId');
	window.addEventListener('load', populateCategories);

	function populateCategories() {
		let url = "../transaction/getcategory?userId="+userId+"&transactionTypeId="+transactionTypeId;
	    fetch(url, {method:'POST'})
	        .then(response => response.json())
	        .then(categories => {
	            const selectTag = document.getElementById('addCategorySelect');
				const customInputTag = document.getElementById('addCategoryInput');
				customInputTag.style.display = 'none';
	            selectTag.innerHTML = '';
	            
	            const doption = document.createElement('option');
	            doption.value=-1; 
	            doption.textContent = "-- Select --";
	            selectTag.appendChild(doption);
	            
	            const customOption = document.createElement('option');
	            customOption.value = 0;
	            customOption.textContent = "-- Custom Category --";
	            selectTag.appendChild(customOption);

	            Object.entries(categories).forEach(([key, value]) => {
	                const option = document.createElement('option');
	                option.value = key;
	                option.textContent = value;
	                selectTag.appendChild(option);
	            });
	            
	            selectTag.addEventListener('change', function() {
	                if (this.value == 0) {
	                	selectTag.style.display = 'none';
	                    customInputTag.style.display = 'block';
	                }
	            });
	        })
	        .catch(error => console.error('Error fetching categories:', error));
	}

	function validateForm(form) {
	    const amount = form.querySelector('input[name="amount"]').value;
	    const note = form.querySelector('input[name="note"]').value;
	    let date = form.querySelector('input[name="date"]');
	    let time = form.querySelector('input[name="time"]');
	    const category=form.querySelector('select[name="categoryId"]').value;
	    
	    if(category==0 && !form.querySelector('input[name="category"]').value){
	    	alert('Custom category annot be empty.');
	    }
	    
	    if (!amount || isNaN(amount) || amount <= 0) {
	        alert('Amount must be a positive integer.');
	        return false;
	    }

	    if (!note.trim()) {
	        alert('Note cannot be empty.');
	        return false;
	    }
	    
	    if (!category|| category==-1) {
	        alert('Please select a category.');
	        return false;
	    }
	    
	    if(category==0 && !form.querySelector('input[name="category"]').value){
	    	alert('Custom category annot be empty.');
	    	return false;
	    }
	    
	    if(date!=null && time!=null && (!date.value || !time.value )){
	    	alert('Date or Time cannot be empty.');
	    	return false;
	    }
	    
	    
	    

	    return true;
	}

	document.getElementById('addExpenseForm').addEventListener('submit', function(event) {
	    event.preventDefault(); 
	    
	    if (!validateForm(this)) {
	        return;
	    }
	    
	    const selectTag = this.querySelector('select[name="categoryId"]');
		const customInputTag = this.querySelector('input[name="category"]');
		
	    
	    fetch("../transaction/add?userId="+userId+"&transactionTypeId="+transactionTypeId, {
	        method: 'POST',
	        body: new FormData(this)
	    })
	    .then(response => {
	    	
	    	if(selectTag.value==0){
    	    	selectTag.style.display="block";
    			customInputTag.style.display = 'none';
    	    	populateCategories();
    	    }
	    	
	        if (response.ok) {
	            alert('Expense added successfully');
	            this.reset();
	            populateExpenseTable();
	        } 
	        else if(response.status === 400){
	            throw new Error('Wrong Parameter type.');
	        }
	        else if(response.status === 409){
	            alert('Given Category is already exist, Try with another name.');
	            return;
	        }
	        else if(response.status === 500){
	            throw new Error('Error occurred while adding expense');
	        }
	        else{
	        	throw new Error('Failed to add expense');
	        }
	    })
	    .catch(error => {
	        console.error('Error adding expense:', error);
	        alert(error);
	    });
	});

	async function updateExpense(transactionId) {

	    const row = document.getElementById('expenseRow_' + transactionId);
	    const userId = row.querySelector('input[name="userId"]').value;
	    const amount = row.querySelector('input[name="amount"]').value;
	    const note = row.querySelector('input[name="note"]').value;
	    const time = row.querySelector('input[name="time"]').value;

	    document.getElementById('expenseIdInput').value = transactionId;
	    document.getElementById('userIdInput').value = userId;
	    document.getElementById('amountInput').value = amount;
	    document.getElementById('noteInput').value = note;
	    document.getElementById('timeInput').value = time;
	    	
	    try {
	        let url = "../transaction/getcategory?userId="+userId+"&transactionTypeId="+transactionTypeId;
	        const response = await fetch(url);
	        const categories = await response.json();

	        const selectTag = document.getElementById('updateCategorySelect');
	        selectTag.innerHTML = '';

	        Object.entries(categories).forEach(([key, value]) => {
	            const option = document.createElement('option');
	            option.value = key;
	            option.textContent = value;
	            selectTag.appendChild(option);
	        });
	    } catch (error) {
	        console.error('Error fetching categories:', error);
	    }

	    const updateExpenseModal = new bootstrap.Modal(document.getElementById('updateExpenseModal'));
	    updateExpenseModal.show();
	}

	function submitUpdateExpenseForm() {
		const form=document.getElementById('updateExpenseForm');

	    if (!validateForm(form)) {
	        return;
	    }
		
		const formData = new FormData(form);

	    fetch("../transaction/update?transactionTypeId="+transactionTypeId, {
	        method: 'POST',
	        body: formData
	    })
	    .then(response => {
	        if (response.ok) {
	            alert('Expense updated successfully');
	            populateExpenseTable();
	        } else {
	            throw new Error('Failed to update expense');
	        }
	    })
	    .catch(error => {
	        console.error('Error updating expense:', error);
	        alert('Failed to update expense. Please try again.');
	    });

	    const modalElement = document.getElementById('updateExpenseModal');
	    modalElement.style.display = 'none';
	    
	    document.body.classList.remove('modal-open');
	    const modalBackdrop = document.getElementsByClassName('modal-backdrop')[0];
	    modalBackdrop.parentNode.removeChild(modalBackdrop); 
	}

	function removeExpense(transactionId) {

	    fetch("../transaction/remove?transactionId="+transactionId)
	    .then(response => {
	        if (response.ok) {
	            alert('Expense deleted successfully');
	            populateExpenseTable();
	        } else if(response.status === 400){
	            throw new Error('Wrong Parameter type.');
	        }
	        else if(response.status === 500){
	            throw new Error('Error occurred while removing expense');
	        }
	        else{
	        	throw new Error('Failed to remove expense');
	        }
	        
	    })
	    .catch(error => {
	        console.error('Error removing expense:', error);
	        alert('Failed to remove expense. Please try again.');
	    });
	}

	function populateExpenseTable() {
	    let url = "../transaction/get?userId="+userId+"&transactionTypeId="+transactionTypeId;
	    fetch(url, {method:'POST'})
	        .then(response => {
			console.log(response);
			return response.json();
		})
	        .then(data => {
	            const expenseTableBody = document.getElementById('expenseTableBody');
	            expenseTableBody.innerHTML = ''; 
	            let totalExpense = 0;
	            
	            data.forEach(expense => {
	            	totalExpense += parseFloat(expense.amount);
	                const row = document.createElement('tr');
	                row.id = 'expenseRow_' + expense.transactionId;
	                row.innerHTML =
	                    "<input type='hidden' name='userId' value='" + expense.userId + "'>" +
	                    "<input type='hidden' name='categoryId' value='" + expense.categoryId + "'>" +
	                    "<td><input name='amount' value='" + expense.amount + "' readonly></td>" +
	                    "<td><input name='note' value='" + expense.note + "' readonly></td>" +
	                    "<td><input name='category' value='" + expense.category + "' readonly></td>" +
	                    "<td>"+expense.date+"</td>"+
	                    "<td><input name='time' value='" + expense.time + "' readonly></td>" +
	                    "<td>" +
	                    "<button type='button' class='btn btn-sm btn-success' onclick='updateExpense(" + expense.transactionId + ")'>Update</button>" +
	                    "</td>" +
	                    "<td><button class='btn btn-sm btn-danger' onclick='removeExpense(" + expense.transactionId + ")'>Remove</button></td>";

	                expenseTableBody.appendChild(row);
	            });
	            document.getElementById('totalExpense').innerText = "Total Expense: " + totalExpense.toFixed(2);
	        })
	        .catch(error => console.error('Error fetching expense data:', error));
	}

	window.addEventListener('load', populateExpenseTable);

</script>

</body>
</html>
