<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html lang="en">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	
	<script type="text/javascript" src="js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="css/common.css" >


</head>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand active" href="#">Secure Authorized Deduplication</a>
			</div>
			    <a class="navbar-brand active" href="." style="float: right;">Log Out</a>
			    <a class="navbar-brand active jsRefresh" style="float: right;">Refresh</a>
		</div>
	</nav>
<div class="container jsUserId" id="${user.id}">
 <h3>${user.username}</h3>
    <hr>
    <div class="loader hidden"><img src="css/spinner-icon.gif"></div> 
   <div class="alert alert-success hidden jsSuccess">
    <strong>Fie uploaded successfully</strong>  <br><a href="" class="alert-link jsRefresh">To download please refresh page!!</a></br>
  </div>
    <div class="alert alert-danger hidden jsError">
    <strong>Error!</strong> Try it again!!
  </div>
    <div class="row">
    			<div class="">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-6">
								<a href="#" class="active" id="download-link">Download Files</a>
							</div>
							<div class="col-xs-6">
								<a href="#" id="upload-link">Upload Files</a>
							</div>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
        <div id="download-tab" class="panel panel-primary filterable">
            <div class="panel-heading">
                <h3 class="panel-title">Files</h3>
                <div class="pull-right">
                    <button class="btn btn-default btn-xs btn-filter"><span class="glyphicon glyphicon-filter"></span> Filter</button>
                </div>
            </div>
            <table class="table">
                <thead>
                    <tr class="filters">
                        <th><input type="text" class="form-control" placeholder="#" disabled></th>
                        <th><input type="text" class="form-control" placeholder="File Name" disabled></th>
                        <th><input type="text" class="form-control" placeholder="File key" disabled></th>
                        <th><input type="text" class="form-control" placeholder="Download" disabled></th>
                    </tr>
                </thead>
                <tbody>
                <c:set var="count" value="0" scope="page" />
                <c:forEach var="file" items="${fileList}">
		            <c:set var="count" value="${count + 1}" scope="page"/>
			         <tr>
			            <td>${count}</td>
                        <td>${file.name}</td>
                        <td>	
						<input type="text" name="fileKey" id="${file.fileId}fileKey" class="form-control" value="please insert file key here">
					   </td>
                        <td><button id="${file.fileId}" class="jsDownload">Download</button></td>                      
                    </tr>
			
		         </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div id="upload-tab" class="file-upload" style="display: none;">
	<form id="fileUploadForm" 
      action="/"
      enctype="multipart/form-data">
    <fieldset>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="row">
                <label class="control-label col-md-2 text-right" for="filename"><span>File</span></label>
                <div class="col-md-10">
                    <div class="input-group">
                        <input type="file" id="file" name="file" class="form-control form-control-sm">
                        <div class="input-group-btn">
                            <input type="submit" id ="btnSubmit" value="Upload" class="rounded-0 btn btn-primary">
                        </div>
                    </div>
                </div>
                </div>
            </div>                        
        </div>
    </fieldset>    
</form>

</body>

</html>