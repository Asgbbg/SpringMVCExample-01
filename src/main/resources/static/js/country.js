let mode = "";
let oldCountryCd;

$(document).ready(function() {
	$("#detailContains").css("display", "none");
	// when click the create button, show the detailContains
	$("#selCreate").on('click', function() {
		// clear all input
		$(':input', '#frmDetail')
			.not(':button, :submit, :reset, :hidden')
			.val('');
		// show the detailContains
		$("#detailContains").css("display", "block");
		// hide the queryContainer
		$("#queryContainer").css("display", "none");
		mode = "create";
		$("#updateBtn").text('插入');
		$("#mstcountrycd").prop('readonly', false).val('').css('background-color', '')
		$("#mstcountrynanme").prop('readonly', false).val('').css('background-color', '');
	});

	// when click the update button, show the queryContainer
	$("#selUpdate, #selDelete").on('click', function(event) {
		// show the queryContainer
		$("#queryContainer").css("display", "block");
		// hide the detailContains
		$("#detailContains").css("display", "none");
		$("#queryInput").val('');
		$("#mstcountrycd").prop('readonly', true).css('background-color', '#D3D3D3');
		if (event.target.id === 'selUpdate') {
			$("#updateBtn").text('更新');
			mode = "update";
		} else if (event.target.id === 'selDelete') {
			mode = "delete";
			$("#updateBtn").text('删除');
			$("#mstcountrynanme").prop('readonly', true).val('').css('background-color', '#D3D3D3');
		}
	});

	$("#queryBtn").on('click', function() {
		// use ajax to post data to controller
		// recived the data from controller with json
		// show the data in the detailContains
		$.ajax({
			type: "POST",
			url: "/country/getCountry",        //  <- controller function name
			data: $("#frmSearch").serialize(),
			dataType: 'json',
			success: function(data) {
				$("#detailContains").css("display", "block");
				// show the data in the detailContains
				$("#mstcountrycd").val(data.mstcountrycd);
				$("#mstcountrynanme").val(data.mstcountrynanme);
				oldCountryCd = data.mstcountrycd;
			},
			error: function(data) {
				alert("出现了错误：" + data.responseJSON.message);
			}
		});
	});

	$("#updateBtn").click(function() {
		var formData = $("#countryDetail").serialize();
		if (mode === "create") {
			$.ajax({
				type: "POST",
				url: "/addCountry",
				data: formData,
				success: function(data) {
					alert("插入了" + data + "条数据");
					$("#detailContains").css("display", "none");
				},
				error: function(data) {
					alert("出现了错误：" + data.responseJSON.message);
				}
			});
		} else if (mode === "update") {
			$.ajax({
				type: "POST",
				url: "/updateCountry",
				data: formData,
				success: function(data) {
					alert("更新了" + data + "条数据");
					$("#detailContains").css("display", "none");
				},
				error: function(data) {
					alert("出现了错误：" + data.responseJSON.message);
				}
			});
		} else if (mode === "delete") {
			let result = confirm("确定要删除吗");
			if (result) {
				$.ajax({
					type: "POST",
					url: "/deleteCountry",
					data: formData,
					success: function(data) {
						alert("删除了" + data + "条数据");
						$("#detailContains").css("display", "none");
					},
					error: function(data) {
						alert("出现了错误：" + data.responseJSON.message);
					}
				});
			}
		}

	});
	
	$("#clearBtn").click(function() {
		$("#queryInput").val('');
		$("#detailContains").css("display", "none");
	});

	$("#returnBtn").on('click', function() {
		$("#detailContains").css("display", "none");
	});

});