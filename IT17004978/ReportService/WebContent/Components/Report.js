$(document).ready(function() {
	if ($("#alertSuccess").text().trim() == "") {
		$("#alertSuccess").hide();
	}

	$("#alertError").hide();

	$('#Name').attr('maxlength', 16);

	$('#Number').attr('maxlength', 10);

	// space by 4 digits
	/*
	
	 *//*
		 * $('#cardNo').on('keypress change', function () { $(this).val(function
		 * (index, value) { return value.replace(/\W/gi, '').replace(/(.{4})/g,
		 * '$1 '); }); });
		 */

});


$(document).on("click", "#btnSave", function(event) {
	// Clear alerts
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();

	// Form validation
	var status = validateItemForm();

	if (status != true) {
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}

	// If valid
	var method = ($("#hidItemIDSave").val() == "") ? "POST" : "PUT";

	$.ajax({
		url : "ReportService",
		type : method,
		data : $("#ReportForm").serialize(),
		dataType : "text",
		complete : function(response, status) {
			onItemSaveComplete(response.responseText, status);
		}
	});
});


$(document)
		.on(
				"click",
				".btnUpdate",
				function(event) {
					$("#hidItemIDSave").val(
							$(this).closest("tr").find('#hidItemIDUpdate')
									.val());
					$("#name").val(
							$(this).closest("tr").find('td:eq(0)').text());
					$("#dep").val(
							$(this).closest("tr").find('td:eq(1)').text());
					$("#address").val(
							$(this).closest("tr").find('td:eq(2)').text());
					$("#number")
							.val($(this).closest("tr").find('td:eq(3)').text());
				});

function onItemSaveComplete(response, status) {
	if (status == "success") {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success") {
			$("#alertSuccess").text("Successfully saved !");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error") {
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error") {
		$("#alertError").text("Error while saving.");
		$("#alertError").show();
	} else {
		$("#alertError").text("Unknown saving erro....?");
		$("#alertError").show();
	}

	$("#hidItemIDSave").val("");
	$("#DoctorForm")[0].reset();
}

$(document).on("click", ".btnRemove", function(event) {
	$.ajax({
		url : "ReportService",
		type : "DELETE",
		data : "itemID=" + $(this).data("itemid"),
		dataType : "text",
		complete : function(response, status) {
			onItemDeleteComplete(response.responseText, status);
		}
	});
});

function onItemDeleteComplete(response, status) {
	if (status == "success") {
		var resultSet = JSON.parse(response);

		if (resultSet.status.trim() == "success") {
			$("#alertSuccess").text("Successfully deleted !");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error") {
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error") {
		$("#alertError").text("Error while deleting.");
		$("#alertError").show();
	} else {
		$("#alertError").text("Unknown deleting error....?");
		$("#alertError").show();
	}
}

function validateItemForm() {
	// Name
	if ($("#name").val().trim() == "") {
		return "Insert Full Name.";
	}

	var tmpNo = $("#name").val().trim();

	if (!$.isNumeric(tmpNo)) {
		return "Insert full Name.";
	}
	// length validate
	if ($("#name").val().length > 30) {
		return "The name you entered exceed the length"
	}
	if ($("#name").val().length < 16) {
		return "The name you entered Doesn't meet the length"
	}

	// Reason
	if ($("#dep").val().trim() == "") {
		return "Insert the Reason.";
	}
	if ($("#dep").val().length > 15) {
		return "The Reason is too long"
	}

	// Address
	if ($("#address").val().trim() == "") {
		return "Insert Adddess.";
	}

	// CVC
	if ($("#number").val().trim() == "") {
		return "Insert Phone Number.";
	}
	// is numerical value
	var tmpCvc = $("#number").val().trim();
	$('#number').attr('maxlength', 10);
	if (!$.isNumeric(tmpCvc)) {
		return "Insert Phone Number.";
	}
	// length validate
	if ($("#number").val().length > 3) {
		return "The Phone Number too long."
	}
	if ($("#number").val().length < 3) {
		return "The Phone Number too short"
	}

	return true;
}