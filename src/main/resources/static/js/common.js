$(function() {

	$('#login-form-link').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
		$("#register-form").fadeOut(100);
		$('#register-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#register-form-link').click(function(e) {
		$("#register-form").delay(100).fadeIn(100);
		$("#login-form").fadeOut(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});

});
$(function() {

	$('#download-link').click(function(e) {
		$("#download-tab").delay(100).fadeIn(100);
		$("#upload-tab").fadeOut(100);
		$("#upload-tab").removeClass('active');
		$("#upload-link").removeClass('active');
		$("#download-link").addClass('active')
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#upload-link').click(function(e) {
		$("#upload-tab").delay(100).fadeIn(100);
		$("#download-tab").fadeOut(100);
		$("#download-tab").removeClass('active');
		$("#upload-link").addClass('active');
		$("#download-link").removeClass('active')
		$(this).addClass('active');
		e.preventDefault();
	});

});

$(document).on(
		"click",
		'.jsDownload',
		function() {
			 $(".jsSuccess").addClass("hidden")
			  $(".jsError").addClass("hidden")
			var userId = $(".jsUserId").attr("id");
			var fileId = $(this).attr("id");
			var $row = $(this).closest('tr');
			var $columns = $row.find('td');
			var values = [];
			$.each($columns, function(i, item) {
				values[i] = item.innerHTML;
			});
			var fileName = values[1];
			var fileKey=$("#"+fileId+"fileKey").val();
			if (userId && fileId && fileName && fileKey) {
				window.open("/secure-auth-d/files?userId=" + userId
						+ "&fileId=" + fileId + "&fileName=" + fileName +"&fileKey=" +fileKey);
			}

		});

$(document)
		.ready(
				function() {
					$('.filterable .btn-filter')
							.click(
									function() {
										var $panel = $(this).parents(
												'.filterable'), $filters = $panel
												.find('.filters input'), $tbody = $panel
												.find('.table tbody');
										if ($filters.prop('disabled') == true) {
											$filters.prop('disabled', false);
											$filters.first().focus();
										} else {
											$filters.val('').prop('disabled',
													true);
											$tbody.find('.no-result').remove();
											$tbody.find('tr').show();
										}
									});

					$('.filterable .filters input')
							.keyup(
									function(e) {
										/* Ignore tab key */
										var code = e.keyCode || e.which;
										if (code == '9')
											return;
										/* Useful DOM data and selectors */
										var $input = $(this), inputContent = $input
												.val().toLowerCase(), $panel = $input
												.parents('.filterable'), column = $panel
												.find('.filters th').index(
														$input.parents('th')), $table = $panel
												.find('.table'), $rows = $table
												.find('tbody tr');
										/* Dirtiest filter function ever ;) */
										var $filteredRows = $rows
												.filter(function() {
													var value = $(this).find(
															'td').eq(column)
															.text()
															.toLowerCase();
													return value
															.indexOf(inputContent) === -1;
												});
										/* Clean previous no-result if exist */
										$table.find('tbody .no-result')
												.remove();
										/*
										 * Show all rows, hide filtered ones
										 * (never do that outside of a demo !
										 * xD)
										 */
										$rows.show();
										$filteredRows.hide();
										/*
										 * Prepend no-result row if all rows are
										 * filtered
										 */
										if ($filteredRows.length === $rows.length) {
											$table
													.find('tbody')
													.prepend(
															$('<tr class="no-result text-center"><td colspan="'
																	+ $table
																			.find('.filters th').length
																	+ '">No result found</td></tr>'));
										}
									});
				});

// file upload functionaly

$(document).on("click", '#btnSubmit', function() {
	event.preventDefault();
	$('.loader').removeClass("hidden");
	 $(".jsSuccess").addClass("hidden")
	  $(".jsError").addClass("hidden")
  var form=$("#fileUploadForm")[0];
	var data = new FormData(form);	
	var userId = $(".jsUserId").attr("id");
	$.ajax({
		type : "POST",
		enctype : 'multipart/form-data',
		url : "/secure-auth-d/files?userId="+userId,
		data : data,
		processData : false, 
		contentType : false,
		cache : false,
		timeout : 600000,
		success : function(data) {		
        $(".jsSuccess").removeClass("hidden")
    	$('.loader').addClass("hidden");

		},
		error : function(response) {
			 var r = jQuery.parseJSON(response.responseText);
			 $(".jsError").removeClass("hidden");
            $(".jsError").text(r.message);
        	$('.loader').addClass("hidden");
		}
	});

});
$(document).on("click", '.jsRefresh', function() {
	$('.loader').removeClass("hidden");
	event.preventDefault();
	location.reload(true); 
	$('.loader').addClass("hidden");

});

