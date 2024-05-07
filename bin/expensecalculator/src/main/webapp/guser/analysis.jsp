<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Analysis</title>
<!-- Include any necessary CSS or JavaScript libraries -->
</head>
<body>
<%@include file="../includes/navbar.jsp" %>

	<div class="container my-3">
		
		<form id="filterForm">
		<table class="table table-light">
			<tbody>
				<tr>
						<td>Start Date :</td>
						<td><input type="date" name="sdate" class="form-control"
							required></td>
						<td>End Date :</td>
						<td><input type="date" name="edate" class="form-control"
							required></td>
						<td>
							<button type="submit" class="btn btn-sm btn-primary">Filter</button>
						</td>
				</tr>
			</tbody>
		</table>
		</form>
		<div class="d-flex py-3">
			<h3 id="balance">Balance:</h3>
		</div>

	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6 border p-3">
				<div class="row">
					<div class="col-md-6">
						<h3 id="totalExpense">Total Expense:</h3>
					</div>


					<div class="col-md-6 d-flex align-items-center">
						<span class="me-4">Select Category:</span> 
						<select	name="expenseCategory" id="expenseCategorySelect" class="form-control" disabled></select>
					</div>
				</div>
				<table class="table table-striped mx-2">
					<thead>
						<tr>
							<th scope="col">Expense</th>
							<th scope="col">Note</th>
							<th scope="col">Category</th>
							<th scope="col">Date</th>
							<th scope="col">Time</th>
						</tr>
					</thead>
					<tbody id="expenseTableBody"></tbody>
				</table>
			</div>

			<div class="col-md-6 border p-3">
				<div class="row">
					<div class="col-md-6">
						<h3 id="totalIncome">Total Income:</h3>
					</div>


					<div class="col-md-6 d-flex align-items-center">
						<span class="me-4">Select Category:</span> 
						<select	name="incomeCategory" id="incomeCategorySelect" class="form-control" disabled></select>
					</div>
				</div>
				<table class="table table-striped mx-2">
					<thead>
						<tr>
							<th scope="col">Income</th>
							<th scope="col">Note</th>
							<th scope="col">Category</th>
							<th scope="col">Date</th>
							<th scope="col">Time</th>
						</tr>
					</thead>
					<tbody id="incomeTableBody"></tbody>
				</table>
			</div>
		</div>
	</div>

	<script>
	
	const userId = sessionStorage.getItem('userId');
	var expenseTypeId=1;
	var incomeTypeId=2;
	var expenseCategory = "expenseCategory";
	var incomeCategory = "incomeCategory";
	var expenseTableBody = "expenseTableBody";
	var incomeTableBody = "incomeTableBody";
	var sdate;
	var edate;
	var expenseAmount=0;
	var incomeAmount=0;
	var expenseCategoryId=0;
	var incomeCategoryId=0;

	window.addEventListener('load', populateCategories(expenseTypeId, expenseCategory));
	window.addEventListener('load', populateCategories(incomeTypeId, incomeCategory));

	function populateTables() {
	    fetch("../transaction/getfiltered?userId=" + userId + "&sdate=" + sdate + "&edate=" + edate + "&categoryId=0&transactionTypeId=0")
	        .then(response => response.json())
	        .then(data => {
	            populateBothTable(data);
	            console.log("Tables populated successfully");
	        })
	        .catch(error => {
	            console.error("Error populating tables:", error);
	        });
	}
	
	function populateBothTable(data) {
	    const expenseTableBody = document.getElementById("expenseTableBody");
	    const incomeTableBody = document.getElementById("incomeTableBody");
	    
	    expenseTableBody.innerHTML = '';
	    incomeTableBody.innerHTML = '';
	    
	    expenseAmount = 0;
	    incomeAmount = 0;

	    data.forEach(transaction => {
	        const row = document.createElement('tr');
	        row.innerHTML =
	            "<td>" + transaction.amount + "</td>" +
	            "<td>" + transaction.note + "</td>" +
	            "<td>" + transaction.category + "</td>" +
	            "<td>" + transaction.date + "</td>" +
	            "<td>" + transaction.time + "</td>";

	        if (transaction.typeId === expenseTypeId) {
	            expenseTableBody.appendChild(row);
	            expenseAmount += parseFloat(transaction.amount);
	        } else if (transaction.typeId === incomeTypeId) {
	            incomeTableBody.appendChild(row);
	            incomeAmount += parseFloat(transaction.amount);
	        }
	    });

	    document.getElementById('totalExpense').innerText = "Total Expense: " + expenseAmount.toFixed(2);
	    document.getElementById('totalIncome').innerText = "Total Income: " + incomeAmount.toFixed(2);
	    updateBalance(incomeAmount, expenseAmount);
	}

	function updateBalance(incomeAmount, expenseAmount) {
	    const balance = incomeAmount - expenseAmount;
	    
	    document.getElementById('balance').innerText = "Balance: " + incomeAmount + " - " + expenseAmount + " = " + balance.toFixed(2);
	}

	function validateForm(form) {
	    const sdate = form.querySelector('input[name="sdate"]').value;
	    const edate = form.querySelector('input[name="edate"]').value;

	    if (!sdate || !edate) {
	        alert('Start Date and End Date cannot be empty.');
	        return false;
	    }

	    return true;
	}

	document.getElementById("filterForm").addEventListener("submit", function(event) {
	    event.preventDefault();
	    
	    if(!validateForm(this)){
	    	return;
	    }
	    
		
	    const formData = new FormData(this);

	    sdate = formData.get("sdate");
	    edate = formData.get("edate");
	    expenseCategoryId=0;
	    incomeCategoryId=0;
	    let expenseCategory= document.getElementById("expenseCategorySelect");
	    let incomeCategory = document.getElementById("incomeCategorySelect");
	    expenseCategory.disabled = false;
	    incomeCategory.disabled = false;
	    expenseCategory.options[0].selected=true;
	    incomeCategory.options[0].selected=true;
	    populateTables();
	    
	});

	function populateTable(sdate, edate, categoryId, transactionTypeId, tableBodyName) {
	   return  fetch("../transaction/getfiltered?userId=" + userId + "&sdate=" + sdate + "&edate=" + edate+ "&categoryId=" + categoryId+ "&transactionTypeId="+transactionTypeId)
	        .then(response => response.json())
	        .then(data => {
	            const tableBody = document.getElementById(tableBodyName);
	            tableBody.innerHTML = ''; 
	            let total = 0;
	            
	            data.forEach(transcation => {
	            	total += parseFloat(transcation.amount);
	                const row = document.createElement('tr');
	                row.id = 'row_' + transcation.tranactionId;
	                row.innerHTML =
	                    "<td>" + transcation.amount + "</td>" +
	                    "<td>" + transcation.note + "</td>" +
	                    "<td>" + transcation.category + "</td>" +
	                    "<td>"+transcation.date+"</td>"+
	                    "<td>" + transcation.time + "</td>";

	                    tableBody.appendChild(row);
	            });

	            if(transactionTypeId==expenseTypeId){
	                expenseAmount=total.toFixed(2);
	                document.getElementById('totalExpense').innerText = "Total Expense: " + total.toFixed(2);
	            }
	            else{
	                incomeAmount=total.toFixed(2);
	                document.getElementById('totalIncome').innerText = "Total Income: " + total.toFixed(2);
	            }
	            
	        })
	        .catch(error => console.error('Error fetching table data:', error));
	}

	document.querySelector('select[name="expenseCategory"]').addEventListener('change', function(event) {
		event.preventDefault();
		expenseCategoryId = this.value;
		populateTable(sdate, edate, expenseCategoryId, expenseTypeId, expenseTableBody )
		.then(() => {
	        updateBalance(incomeAmount, expenseAmount);
	    });
	});

	function populateCategories(transactionTypeId, categoryName) {
	    fetch("../transaction/getcategory?userId="+userId+"&transactionTypeId="+transactionTypeId)
	        .then(response => response.json())
	        .then(categories => {
	            const selectTag = document.querySelector('select[name="'+categoryName+'"]');
	            selectTag.innerHTML = ''; 
	            
	            const doption = document.createElement('option');
	            doption.value = 0; 
	            doption.textContent = "All";
	            selectTag.appendChild(doption);
	            
	            Object.entries(categories).forEach(([key, value]) => {
	                const option = document.createElement('option');
	                option.value = key;
	                option.textContent = value;
	                selectTag.appendChild(option);
	            });
	        })
	        .catch(error => console.error('Error fetching categories:', error));
	}


	document.querySelector('select[name="incomeCategory"]').addEventListener('change', function(event) {
		event.preventDefault();
		incomeCategoryId = this.value;
		populateTable(sdate, edate, incomeCategoryId, incomeTypeId, incomeTableBody )
	    .then(() => {
	        updateBalance(incomeAmount, expenseAmount);
	    });
	});
</script>

</body>
</html>