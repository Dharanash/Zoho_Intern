<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Income</title>
    <!-- Include any necessary CSS or JavaScript libraries -->
</head>
<body>
<%@include file="../includes/navbar.jsp" %>

<!-- Modal -->
<div class="modal fade" id="updateIncomeModal" tabindex="-1" aria-labelledby="updateIncomeModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateExpenseModalLabel">Update Income</h5>
        <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="updateIncomeForm">
          <input type="hidden" class="form-control" name="transactionId" id="incomeIdInput">
          <input type="hidden" class="form-control" name="autoAdderStatusId" id="autoAdderStatusId">

          <label>Amount:</label>
          <input type="number" class="form-control" name="amount" id="amountInput" required>

          <label>Note:</label>
          <input type="text" class="form-control" name="note" id="noteInput" required>
          
          <label>Category:</label>
          <select name="categoryId" class="form-control" id="updateCategorySelect" required></select>

          <label>Date:</label>
          <input type="datetime-local" class="form-control" name="datetime" id="datetimeInput" required>
        
          <label>Repeater:</label>
         <input type="checkbox" name="autoAdder" class="form-control" id="autoAdderCheckboxUpdate" onclick="autoAdderClickUpdate(this)">
			<input type="number" min="1" name="autoAdderCount" id="autoAdderCountUpdate" placeholder="Repeat" class="form-control" style="display: none;">
			<select name="autoAdderCategoryId" id="autoAdderCategoryUpdate" class="form-control" style="display: none;"></select>

        
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="submitUpdateIncomeForm()">Save changes</button>
      </div>
    </div>
  </div>
</div>

<div class="container my-3">
   <form id="addIncomeForm">
    <table class="table table-light">
        <thead>
            <tr>
                <th scope="col">Income Amount</th>
                <th scope="col">Note</th>
                <th scope="col">Category</th>
                <th scope="col">DateTime</th>
				<th scope="col">Repeater</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                
                <td><input type="number" name="amount" class="form-control" min="1" step="any" required></td>
                <td><input type="text" name="note" class="form-control" required></td>
                <td><select name="categoryId" id="addCategorySelect" class="form-control" required></select>
                <input type="text" class="form-control" name="category" id="addCategoryInput" placeholder="Custom Category"></td>
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
        <h3 id="totalIncome">Total Income:</h3>
   
    </div>
    <br>
    <table class="table table-striped">
        <thead>
        <tr>
			<th scope="col">Repeater</th>
            <th scope="col">Amount</th>
            <th scope="col">Note</th>
            <th scope="col">Category</th>
            <th scope="col">Date Time</th>
            <th scope="col">Update</th>
            <th scope="col">Remove</th>
        </tr>
        </thead>
        <tbody id="incomeTableBody"></tbody>
    </table>
</div>

<script>
var transactionTypeId = 2;
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


document.getElementById('addIncomeForm').addEventListener('submit', function(event) {
	event.preventDefault();
	 if (!validateForm(this)) {
	        return;
	    }
	 	const selectTag = this.querySelector('select[name="categoryId"]');
		const customInputTag = this.querySelector('input[name="category"]');
        const checkbox = this.querySelector('input[name="autoAdder"]');

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

        if (checkbox.checked) {
            this.querySelector('input[name="autoAdderCount"]').style.display = 'none';
            this.querySelector('select[name="autoAdderCategoryId"]').style.display = 'none';
        }
    	
        if (response.ok) {
            alert('Income added successfully.');
            this.reset();
            populateIncomeTable();

        } else if(response.status === 400){
            throw new Error('Wrong Parameter type.');
        }
        else if(response.status === 409){
        	alert('Given Category is already exist, Try with another name.');
            return;
        }
        else if(response.status === 500){
            throw new Error('Error occurred while adding income');
        }
        else{
        	throw new Error('Failed to add income');
        }
    })
    .catch(error => {
        console.error('Error adding income:', error);
        alert('Failed to add income. Please try again.');
    });
});

async function updateIncome(transactionId) {
    try {
        
        const transactionResponse = await fetch("../transaction/gettransaction?userId="+userId+"&transactionId="+transactionId);
        const transactionData = await transactionResponse.json();

        document.getElementById('incomeIdInput').value = transactionData.transactionId;
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

        const updateExpenseModal = new bootstrap.Modal(document.getElementById('updateIncomeModal'));
        updateExpenseModal.show();
    } catch (error) {
        console.error('Error updating expense:', error);
    }
}


function submitUpdateIncomeForm() {
	const form=document.getElementById('updateIncomeForm')
    
    if(!validateForm(form)){
		return;
	}
	
	const formData = new FormData(form);

    fetch("../transaction/update?userId="+userId+"&transactionTypeId="+transactionTypeId, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            alert('Income updated successfully');
            form.reset();
            populateIncomeTable();
            
        } else {
            throw new Error('Failed to update expense');
        }
    })
    .catch(error => {
        console.error('Error updating expense:', error);
        alert('Failed to update expense. Please try again.');
    });

    const modalElement = document.getElementById('updateIncomeModal');
    modalElement.style.display = 'none';
    
    document.body.classList.remove('modal-open');
    const modalBackdrop = document.getElementsByClassName('modal-backdrop')[0];
    modalBackdrop.parentNode.removeChild(modalBackdrop); 
}

function removeIncome(transactionId) {

    fetch("../transaction/remove?transactionId="+transactionId)
    .then(response => {
        if (response.ok) {
            alert('Income deleted successfully');
            populateIncomeTable();
        } else if(response.status === 400){
            throw new Error('Wrong Parameter type.');
        }
        else if(response.status === 500){
            throw new Error('Error occurred while removing income');
        }
        else{
        	throw new Error('Failed to remove income');
        }
    })
    .catch(error => {
        console.error('Error updating income:', error);
        alert('Failed to update income. Please try again.');
    });
}

function populateIncomeTable() {
    let url = "../transaction/get?userId="+userId+"&transactionTypeId="+transactionTypeId;
	    fetch(url, {method:'POST'})
        .then(response => response.json())
        .then(data => {
            const incomeTableBody = document.getElementById('incomeTableBody');
            incomeTableBody.innerHTML = '';
            let totalIncome = 0;
            
            data.forEach(income => {
            	totalIncome += parseFloat(income.amount);
                const row = document.createElement('tr');
                row.id = 'incomeRow_' + income.transactionId;
                row.innerHTML =
                    "<td>"+income.autoAdderStatusString+"</td>" +
					"<td><input  name='amount' value='" + income.amount + "' readonly></td>" +
                    "<td><input  name='note' value='" + income.note + "' readonly></td>" +
                    "<td><input  name='category' value='" + income.category + "' readonly></td>" +
                    "<td><input name='datetime' value='" + income.datetime + "' readonly></td>"+
                    "<td>" +
                    "<button type='button' class='btn btn-sm btn-success' onclick='updateIncome(" + income.transactionId + ")'>Update</button>" +
                    "</td>" +
                    "<td><button class='btn btn-sm btn-danger' onclick='removeIncome(" + income.transactionId + ")'>Remove</button></td>";
					incomeTableBody.appendChild(row);
            });
            document.getElementById('totalIncome').innerText = "Total Income: " + totalIncome.toFixed(2);
        })
        .catch(error => console.error('Error fetching income data:', error));
}

window.addEventListener('load', populateIncomeTable);

</script>
</body>
</html>