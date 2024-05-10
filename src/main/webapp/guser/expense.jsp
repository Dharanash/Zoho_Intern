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
  <style>

   .popup-form-container {
  display: none;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
}

.popup-content {
  width: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.close {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
}
  </style>
  
</head>
<body>
<%@include file="../includes/navbar.jsp" %>

<div id="popupForm" class="popup-form-container">
  <div class="card popup-content">
    <div class="card-header">
      <h5 class="card-title">Update Expense</h5>
      <button type="button" class="close" id="closePopupBtn">&times;</button>
    </div>
    <div class="card-body">
      <form id="updateExpenseForm">
          <input type="hidden" class="form-control" name="transactionId" id="expenseIdInput">
		  <input type="hidden" class="form-control" name="autoAdderStatusId" id="autoAdderStatusId">
          <label>Amount:</label>
          <input type="number" class="form-control" name="amount" id="amountInput" required>

          <label>Note:</label>
          <input type="text" class="form-control" name="note" id="noteInput" required>
         
          <label>Category:</label>
          <select name="categoryId" class="form-control" id="updateCategorySelect" required></select>

          <label>Date:</label>
          <input type="datetime-local" class="form-control" name="datetime"  id="datetimeInput" required>
         
         <label>Repeater:</label>
         <input type="checkbox" name="autoAdder" class="form-control" id="autoAdderCheckboxUpdate" onclick="autoAdderClickUpdate(this)">
			<input type="number" min="1" name="autoAdderCount" id="autoAdderCountUpdate" placeholder="Repeat" class="form-control" style="display: none;">
			<select name="autoAdderCategoryId" id="autoAdderCategoryUpdate" class="form-control" style="display: none;"></select>

        </form>
    </div>
    <div class="card-footer">
      <button type="button" class="btn btn-primary" onclick="submitUpdateExpenseForm()">Save changes</button>
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
					<th scope="col">DateTime</th>
					<th scope="col">Repeater</th>
				</tr>
			</thead>
			<tbody>
				<tr>
						<td><input type="number" class="form-control" name="amount" 
							min="1" step="any" required></td>
						<td><input type="text" class="form-control" name="note" 
							required></td>
						<td><select name="categoryId" id="addCategorySelect" class="form-control" required></select>
						</td>
						<td><input type="datetime-local" class="form-control" name="datetime" id="datetimeInputAdd" required></td>
						<td><input type="checkbox" name="autoAdder" class="form-control" onclick="autoAdderClick(this)">
						<input type="number" min="1" name="autoAdderCount" id="autoAdderCountInput" class="form-control" style="display: none;" placeholder="Repeat">
						<select name="autoAdderCategoryId" id="autoAdderCategorySelect" class="form-control" style="display: none;"></select></td>
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
					<th scope="col">Repeater</th>
					<th scope="col">Amount</th>
					<th scope="col">Note</th>
					<th scope="col">Category</th>
					<th scope="col">DateTime</th>
					<th scope="col">Update</th>
					<th scope="col">Remove</th>
				</tr>
			</thead>
			<tbody id="expenseTableBody"></tbody>
		</table>
	</div>

	<script>
	var transactionTypeId = 1;
	var checked=1;
	var unchecked=2;
	var repeated=3;
	var userId = sessionStorage.getItem('userId');
	window.addEventListener('load', populateCategories);
	window.addEventListener('load', populateAutoAdderCategories('autoAdderCategorySelect'));
	window.addEventListener('load', populateAutoAdderCategories('autoAdderCategoryUpdate'));
	
	function populateAutoAdderCategories(selectTagId){
		let url = "../transaction/getautoaddercategory?userId="+userId;
		fetch(url, {method:'POST'})
        .then(response => response.json())
        .then(categories => {
            const selectTag = document.getElementById(selectTagId);
            selectTag.innerHTML = '';

            Object.entries(categories).forEach(([key, value]) => {
                const option = document.createElement('option');
                option.value = key;
                option.textContent = value;
                selectTag.appendChild(option);
            });

        })
        .catch(error => console.error('Error fetching auto adder categories:', error));
}
	
	function autoAdderClick(checkbox)
	{
		const selectTag = document.getElementById('autoAdderCategorySelect');
		const count = document.getElementById('autoAdderCountInput');
		
		 if (checkbox.checked) {
		selectTag.style.display = 'block';
		count.style.display = 'block';
		 }
		 else{
			 selectTag.style.display = 'none';
			count.style.display = 'none';
		 }
		 
	}
	
	function autoAdderClickUpdate(checkbox)
	{
		const selectTag = document.getElementById('autoAdderCategoryUpdate');
		const count = document.getElementById('autoAdderCountUpdate');
		const status = document.getElementById('autoAdderStatusId');
		
		 if (checkbox.checked) {
			 status.value=1;
		selectTag.style.display = 'block';
		count.style.display = 'block';
		 }
		 else{
			 status.value=2;
			 selectTag.style.display = 'none';
			count.style.display = 'none';
		 }		 
	}
	
	function populateCategories() {
		let url = "../transaction/getcategory?userId="+userId+"&transactionTypeId="+transactionTypeId;
	    fetch(url, {method:'POST'})
	        .then(response => response.json())
	        .then(categories => {
	            const selectTag = document.getElementById('addCategorySelect');
	            selectTag.innerHTML = '';
	            
	            const doption = document.createElement('option');
	            doption.value=-1; 
	            doption.textContent = "-- Select --";
	            selectTag.appendChild(doption);

	            Object.entries(categories).forEach(([key, value]) => {
	                const option = document.createElement('option');
	                option.value = key;
	                option.textContent = value;
	                selectTag.appendChild(option);
	            });
	            
	            selectTag.addEventListener('change', function() {
	             var now = new Date();
	             var istOffset = 5.5 * 60 * 60 * 1000;
	             var istTime = new Date(now.getTime() + istOffset);
	             var formattedDateTime = istTime.toISOString().slice(0, 16);
	                document.getElementById('datetimeInputAdd').value =formattedDateTime; 
	            });
	        })
	        .catch(error => console.error('Error fetching categories:', error));
	}

	function validateForm(form) {
	    const amount = form.querySelector('input[name="amount"]').value;
	    const note = form.querySelector('input[name="note"]').value;
	    const datetime = form.querySelector('input[name="datetime"]').value;
	    const category=form.querySelector('select[name="categoryId"]').value;
	    const checkbox=form.querySelector('input[name="autoAdder"]');
	    
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
	    
	    if(!datetime){
	    	alert('Date and Time cannot be empty.');
	    	return false;
	    }
	    
	    if(checkbox.checked){
	    	const autoAdderCategory=form.querySelector('select[name="autoAdderCategoryId"]').value;
	    	const count = form.querySelector('input[name="autoAdderCount"]').value;
	    	
	    	 if (!count || isNaN(count) || count <= 0) {
	 	        alert('Repeat day count must be a positive integer.');
	 	        return false;
	 	    }
	    	 
	    	 if (!autoAdderCategory) {
	 	        alert('Please select anyone of repeater category.');
	 	        return false;
	 	    }
	    }
	    
	    return true;
	}

	document.getElementById('addExpenseForm').addEventListener('submit', function(event) {
	    event.preventDefault(); 
	    
	    if (!validateForm(this)) {
	        return;
	    }
	    
		const checkbox = this.querySelector('input[name="autoAdder"]');
	    
	    fetch("../transaction/add?userId="+userId+"&transactionTypeId="+transactionTypeId, {
	        method: 'POST',
	        body: new FormData(this)
	    })
	    .then(response => {
	    	
	    	 if (checkbox.checked) {
	    		 this.querySelector('input[name="autoAdderCount"]').style.display = 'none';
	    		 this.querySelector('select[name="autoAdderCategoryId"]').style.display = 'none';
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
	
	document.getElementById('closePopupBtn').addEventListener('click', function() {
		  document.getElementById('popupForm').style.display = 'none';
		});

	async function updateExpense(transactionId) {
	    try {
	        
	        const transactionResponse = await fetch("../transaction/gettransaction?userId="+userId+"&transactionId="+transactionId);
	        const transactionData = await transactionResponse.json();

	        document.getElementById('expenseIdInput').value = transactionData.transactionId;
	        document.getElementById('amountInput').value = transactionData.amount;
	        document.getElementById('noteInput').value = transactionData.note;
	        document.getElementById('datetimeInput').value = transactionData.datetime.slice(0, 16).replace('T', ' ');
	        document.getElementById('autoAdderStatusId').value = transactionData.autoAdderStatus;
			
	        const updateCheckBox = document.getElementById('autoAdderCheckboxUpdate');
	        const updateSelectTag = document.getElementById('autoAdderCategoryUpdate');
	        const count = document.getElementById('autoAdderCountUpdate');

	        if (transactionData.autoAdderStatus === 1) {
	            const autoAdderCount = transactionData.count;
	            const autoAdderCategoryId = transactionData.autoAdderCategoryId;
	            
	            updateSelectTag.options[autoAdderCategoryId - 1].selected = true;
	            count.value = autoAdderCount;
	            updateCheckBox.style.display = 'block';
	            updateCheckBox.checked = true;
	            updateSelectTag.style.display = 'block';
	            count.style.display = 'block';
	            
	        } else if(transactionData.autoAdderStatus === 2) {
	        	updateCheckBox.style.display = 'block';
	        	updateCheckBox.checked = false;
	            updateSelectTag.style.display = 'none';
	            count.style.display = 'none';
	        }
	        else
	        {
	        	updateCheckBox.style.display = 'none';
	            updateSelectTag.style.display = 'none';
	            count.style.display = 'none';
	        }
	        
	        let url = "../transaction/getcategory?userId="+userId+"&transactionTypeId="+transactionTypeId;
	        const categoryResponse = await fetch(url);
	        const categories = await categoryResponse.json();

	        const selectTag = document.getElementById('updateCategorySelect');
	        selectTag.innerHTML = '';

	        Object.entries(categories).forEach(([key, value]) => {
	            const option = document.createElement('option');
	            option.value = key;
	            option.textContent = value;
	            selectTag.appendChild(option);
	        });

	        document.getElementById('popupForm').style.display = 'block';
	    } catch (error) {
	        console.error('Error updating expense:', error);
	    }
	}

	function submitUpdateExpenseForm() {
		const form=document.getElementById('updateExpenseForm');

	    if (!validateForm(form)) {
	        return;
	    }
		
		const formData = new FormData(form);

	    fetch("../transaction/update?userId="+userId+"&transactionTypeId="+transactionTypeId, {
	        method: 'POST',
	        body: formData
	    })
	    .then(response => {
	        if (response.ok) {
	            alert('Expense updated successfully');
	            form.reset();
	            populateExpenseTable();
	        } else {
	            throw new Error('Failed to update expense');
	        }
	    })
	    .catch(error => {
	        console.error('Error updating expense:', error);
	        alert('Failed to update expense. Please try again.');
	    });

	    document.getElementById('popupForm').style.display = 'none';
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
	                    "<td>"+expense.autoAdderStatusString+"</td>" +
	                    "<td><input name='amount' value='" + expense.amount + "' readonly></td>" +
	                    "<td><input name='note' value='" + expense.note + "' readonly></td>" +
	                    "<td><input name='category' value='" + expense.category + "' readonly></td>" +
	                    "<td><input name='datetime' value='" + expense.datetime + "' readonly></td>"+
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
