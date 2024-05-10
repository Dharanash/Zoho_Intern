<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.expensecalculator.dto.*"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="../includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
	<%@include file="../includes/navbar.jsp"%>
	<br>
	<br>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<h2 id="userMsg"></h2>
	</div>

	<div class="container my-5">
		<h4 id="balance" class="text-center border-bottom pb-2">Monthly
			Analysis</h4>
		<div class="row listItem">
			<div class="col-md-6">
				<h5 id="totalIncome" class="p-2">Total Income:</h5>
				<h5 id="HighestIncomeCategory" class="p-2">Highest Income
					Category:</h5>
				<div class="left-container border border-primary rounded  p-3">
					<ul id="incomeList" class="list-group">

					</ul>
				</div>
			</div>
			<div class="col-md-6">
				<h5 id="totalExpense" class="p-2">Total Expense:</h5>
				<h5 id="HighestExpenseCategory" class="p-2">Highest Expense
					Category:</h5>
				<div class="right-container border border-primary rounded  p-3">
					<ul id="expenseList" class="list-group">

					</ul>
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		var userName = sessionStorage.getItem('userName');
		var userId = sessionStorage.getItem('userId');
		var userRoleId = sessionStorage.getItem('userRoleId');
		var addAutoRepeaterExecuted = sessionStorage.getItem('addAutoRepeaterExecuted');
		document.getElementById('userMsg').textContent = 'Welcome ' + userName;
			
			window.addEventListener('load', addAutoRepeater());
			window.addEventListener('load', populateTables());
			
			function populateTables() {
			    fetch("../transaction/getbymonth?userId=" + userId)
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
			    const expenseList = document.getElementById("expenseList");
			    const incomeList = document.getElementById("incomeList");
			    
			    expenseList.innerHTML = '';
			    incomeList.innerHTML = '';
			    
			    let expenseAmount = 0;
			    let incomeAmount = 0;
			    let highestExpenseCategory="none";
			    let highestIncomeCategory="none";
			    let maxExpense=0;
			    let maxIncome=0;

			    data.forEach(transaction => {
			        const listItem  = document.createElement('li');
			        listItem.innerHTML = "<strong>" + transaction.category + "</strong>: " + transaction.amount.toFixed(2);
			        listItem.style.listStyleType = "none"; 
			        if (transaction.typeId === 1) {
			            expenseList.appendChild(listItem);
			            expenseAmount += parseFloat(transaction.amount);
			            if(maxExpense<transaction.amount){
			            	maxExpense=transaction.amount;
			            	highestExpenseCategory=transaction.category;
			            }
			            
			        } else if (transaction.typeId === 2) {
			            incomeList.appendChild(listItem);
			            incomeAmount += parseFloat(transaction.amount);
			            if(maxIncome<transaction.amount){
			            	maxIncome=transaction.amount;
			            	highestIncomeCategory=transaction.category;
			            }
			        }
			    });

			    document.getElementById('totalExpense').innerText = "Total Expense: " + expenseAmount.toFixed(2);
			    document.getElementById('totalIncome').innerText = "Total Income: " + incomeAmount.toFixed(2);
			    document.getElementById('HighestIncomeCategory').innerText = "Highest Income Category: " + highestIncomeCategory;
			    document.getElementById('HighestExpenseCategory').innerText = "Highest Expense Category: " + highestExpenseCategory;
			    updateBalance(incomeAmount, expenseAmount);
			}
			
			function updateBalance(incomeAmount, expenseAmount) {
			    const balance = incomeAmount - expenseAmount;

			    const balanceElement = document.getElementById('balance');
			 	var currentMonthName = new Date().toLocaleString('default', { month: 'long' });
			    balanceElement.innerText = currentMonthName+" Month Analysis : " + incomeAmount + " - " + expenseAmount + " = " + balance.toFixed(2);

			    if (balance < 0) {
			        balanceElement.classList.remove('text-success'); 
			        balanceElement.classList.add('text-danger'); 
			    } else {
			        balanceElement.classList.remove('text-danger'); 
			        balanceElement.classList.add('text-success'); 
			    }
			}
			
			function addAutoRepeater() {
				        fetch("../transaction/executeautoadder?userId=" + userId)
				            .then(response => response.json())
				            .then(data => {
				                if (data.expenseCount > 0) {
				                    alert(data.expenseCount + " Expenses added by repeater.");
				                }

				                if (data.incomeCount > 0) {
				                    alert(data.incomeCount + " Incomes added by repeater.");
				                }

				                console.log("Repeator added successfully");
				                addAutoRepeaterExecuted = true;
				                sessionStorage.setItem('addAutoRepeaterExecuted', true);
				            })
				            .catch(error => {
				                console.error("Error adding by repeater:", error);
				            });
				}
		
	</script>
</body>
</html>