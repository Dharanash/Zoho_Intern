<%@page import="com.expensecalculator.dto.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
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
        var checked = 1;
        var unchecked = 2;
        var repeated = 3;
        var userId = sessionStorage.getItem('userId');
    
        $(document).ready(function () {
            populateCategories();
            populateAutoAdderCategories('#autoAdderCategorySelect');
            populateAutoAdderCategories('#autoAdderCategoryUpdate');
        });
    
        function populateAutoAdderCategories(selectTagId) {
            let url = "../transaction/getautoaddercategory?userId=" + userId;
            $.post(url, function (categories) {
                const selectTag = $(selectTagId);
                selectTag.empty();
    
                $.each(categories, function (key, value) {
                    $("<option>", {
                        value: key,
                        text: value
                    }).appendTo(selectTag);
                });
            }).fail(function (error) {
                console.error('Error fetching auto adder categories:', error);
            });
        }
    
        function autoAdderClick(checkbox) {
            $("#autoAdderCategorySelect, #autoAdderCountInput").toggle();
        }
    
        function autoAdderClickUpdate(checkbox) {
            const selectTag = $('#autoAdderCategoryUpdate');
            const count = $('#autoAdderCountUpdate');
            const status = $('#autoAdderStatusId');
    
            const isChecked = checkbox.checked;
            status.val(isChecked ? 1 : 2);
            selectTag.toggle(isChecked);
            count.toggle(isChecked);
        }
    
        function populateCategories() {
            let url = "../transaction/getcategory?userId=" + userId + "&transactionTypeId=" + transactionTypeId;
    
            $.post(url, function (categories) {
                const selectTag = $('#addCategorySelect');
                selectTag.empty();
    
                $("<option>", {
                    value: -1,
                    text: "-- Select --"
                }).appendTo(selectTag);
    
                $.each(categories, function (key, value) {
                    $("<option>", {
                        value: key,
                        text: value
                    }).appendTo(selectTag);
                });
    
                selectTag.change(function () {
                    var now = new Date();
                    var istOffset = 5.5 * 60 * 60 * 1000;
                    var istTime = new Date(now.getTime() + istOffset);
                    var formattedDateTime = istTime.toISOString().slice(0, 16);
                    $('#datetimeInputAdd').val(formattedDateTime);
                });
            }).fail(function (xhr, status, error) {
                console.error('Error fetching categories:', error);
            });
        }
    
        function validateForm(form) {
            const amount = $(form).find('input[name="amount"]').val();
            const note = $(form).find('input[name="note"]').val();
            const datetime = $(form).find('input[name="datetime"]').val();
            const category = $(form).find('select[name="categoryId"]').val();
            const checkbox = $(form).find('input[name="autoAdder"]');
    
            if (category == 0 && !$(form).find('input[name="category"]').val()) {
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
    
            if (!category || category == -1) {
                alert('Please select a category.');
                return false;
            }
    
            if (!datetime) {
                alert('Date and Time cannot be empty.');
                return false;
            }
    
            if (checkbox.prop("checked")) {
                const autoAdderCategory = $(form).find('select[name="autoAdderCategoryId"]').val();
                const count = $(form).find('input[name="autoAdderCount"]').val();
    
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
    
        $('#addExpenseForm').submit(function (event) {
            event.preventDefault();
    
            if (!validateForm(this)) {
                return;
            }
    
            const checkbox = $(this).find('input[name="autoAdder"]');
    
            $.ajax({
                url: "../transaction/add?userId=" + userId + "&transactionTypeId=" + transactionTypeId,
                method: 'POST',
                data: new FormData(this),
                processData: false,
                contentType: false,
                success: function (response) {
                    if (checkbox.prop("checked")) {
                        $(this).find('input[name="autoAdderCount"]').hide();
                        $(this).find('select[name="autoAdderCategoryId"]').hide();
                    }
    
                    if (response.ok) {
                        alert('Expense added successfully');
                        this.reset();
                        populateExpenseTable();
                    } else if (response.status === 400) {
                        throw new Error('Wrong Parameter type.');
                    } else if (response.status === 409) {
                        alert('Given Category is already exist, Try with another name.');
                        return;
                    } else if (response.status === 500) {
                        throw new Error('Error occurred while adding expense');
                    } else {
                        throw new Error('Failed to add expense');
                    }
                },
                error: function (xhr, status, error) {
                    console.error('Error adding expense:', error);
                    alert(error);
                }
            });
        });
    
        $('#closePopupBtn').click(function () {
            $('#popupForm').hide();
        });
    
        async function updateExpense(transactionId) {
            try {
    
                const transactionResponse = await fetch("../transaction/gettransaction?userId=" + userId + "&transactionId=" + transactionId);
                const transactionData = await transactionResponse.json();
    
                $('#expenseIdInput').val(transactionData.transactionId);
                $('#amountInput').val(transactionData.amount);
                $('#noteInput').val(transactionData.note);
                $('#datetimeInput').val(transactionData.datetime.slice(0, 16).replace('T', ' '));
                $('#autoAdderStatusId').val(transactionData.autoAdderStatus);
    
                const updateCheckBox = $('#autoAdderCheckboxUpdate');
                const updateSelectTag = $('#autoAdderCategoryUpdate');
                const count = $('#autoAdderCountUpdate');
    
                if (transactionData.autoAdderStatus === 1) {
                    const autoAdderCount = transactionData.count;
                    const autoAdderCategoryId = transactionData.autoAdderCategoryId;
    
                    updateSelectTag.find("option").eq(autoAdderCategoryId - 1).prop("selected", true);
                    count.val(autoAdderCount);
                    updateCheckBox.show();
                    updateCheckBox.prop("checked", true);
                    updateSelectTag.show();
                    count.show();
    
                } else if (transactionData.autoAdderStatus === 2) {
                    updateCheckBox.show();
                    updateCheckBox.prop("checked", false);
                    updateSelectTag.hide();
                    count.hide();
                } else {
                    updateCheckBox.hide();
                    updateSelectTag.hide();
                    count.hide();
                }

                let url = "../transaction/getcategory?userId=" + userId + "&transactionTypeId=" + transactionTypeId;
                const categoryResponse = await fetch(url);
                const categories = await categoryResponse.json();
    
                const selectTag = $('#updateCategorySelect');
                selectTag.empty();
    
                $.each(categories, function (key, value) {
                    $("<option>", {
                        value: key,
                        text: value
                    }).appendTo(selectTag);
                });
    
                $('#popupForm').show();
            } catch (error) {
                console.error('Error updating expense:', error);
            }
        }
    
        function submitUpdateExpenseForm() {
            const form = $('#updateExpenseForm');
    
            if (!validateForm(form[0])) {
                return;
            }
    
            $.ajax({
                url: "../transaction/update?userId=" + userId + "&transactionTypeId=" + transactionTypeId,
                method: 'POST',
                data: form.serialize(),
                success: function () {
                        alert('Expense updated successfully');
                        form[0].reset();
                        populateExpenseTable();
                },
                error: function (xhr, status, error) {
                    console.error('Error updating expense:', error);
                    alert('Failed to update expense. Please try again.');
                },
                complete: function () {
                    $('#popupForm').hide();
                }
            });
        }
    
        function removeExpense(transactionId) {
            $.ajax({
                url: "../transaction/remove?transactionId=" + transactionId,
                success: function (response) {
                    alert('Expense deleted successfully');
                    populateExpenseTable();
                },
                error: function (xhr, status, error) {
                    if (xhr.status === 400) {
                        alert('Wrong Parameter type.');
                    } else if (xhr.status === 500) {
                        alert('Error occurred while removing expense');
                    } else {
                        alert('Failed to remove expense');
                    }
                    console.error('Error removing expense:', error);
                }
            });
        }
    
        function populateExpenseTable() {
            let url = "../transaction/get?userId=" + userId + "&transactionTypeId=" + transactionTypeId;
    
            $.post(url, function (data) {
                const expenseTableBody = $('#expenseTableBody');
                expenseTableBody.empty();
                let totalExpense = 0;
    
                $.each(data, function (index, expense) {
                    totalExpense += parseFloat(expense.amount);
    
                    const row = $("<tr>", {
                        id: "expenseRow_" + expense.id
                    }).html(
                        "<td>" + expense.autoAdderStatusString + "</td>" +
                        "<td><input name='amount' value='" + expense.amount + "' readonly></td>" +
                        "<td><input name='note' value='" + expense.note + "' readonly></td>" +
                        "<td><input name='category' value='" + expense.category + "' readonly></td>" +
                        "<td><input name='datetime' value='" + expense.datetime + "' readonly></td>" +
                        "<td>" +
                        "<button type='button' class='btn btn-sm btn-success' onclick='updateExpense(" + expense.transactionId + ")'>Update</button>" +
                        "</td>" +
                        "<td><button class='btn btn-sm btn-danger' onclick='removeExpense(" + expense.transactionId + ")'>Remove</button></td>"
                    );
    
                    expenseTableBody.append(row);
                });
    
                $('#totalExpense').text("Total Expense: " + totalExpense.toFixed(2));
            }).fail(function (error) {
                console.error('Error fetching expense data:', error);
            });
        }
    
        $(document).ready(function () {
            populateExpenseTable();
        });
        
</script>
        

</body>
</html>
