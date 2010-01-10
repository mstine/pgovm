<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine"%>
<%@page import="com.mattstine.polyglotosgi.vendingmachine.api.Money"%>

<% pageContext.setAttribute("nickle", Money.NICKLE); %> 
<% pageContext.setAttribute("dime", Money.DIME); %> 
<% pageContext.setAttribute("quarter", Money.QUARTER); %> 
<% pageContext.setAttribute("dollar", Money.DOLLAR); %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>Polyglot OSGi Vending Machine</title>
	
</head>

<body>
	<div class="header">
		<h1>Polyglot OSGi Vending Machine</h1>
	</div>
	
	<div class="implementation">
		<form method="post" action="<c:url value="/selectVendingMachine.html"/>">
			<label for="vendingMachine">Implementation</label> 
			<select id="vendingMachine" name="vendingMachine" onChange="this.form.submit()">
				<c:forEach items="${vendingMachines}" var="vendingMachine" varStatus="status">
					<option value="${status.index}" <c:if test="${status.index == index}">selected</c:if>>${vendingMachine}</option>
				</c:forEach>
			</select>
		</form>
	</div>
	
	<div class="status">
		<p>
			<label for="bank">Bank</label>
			<input id="bank" type="text" value="${bank}">
		</p>
		<p>
			<label for="inventory">Inventory</label>
			<input id="inventory" type="text" value="${inventory}">
		</p>
		<p>
			<label for="moneyInserted">Coins Inserted</label>
			<input id="moneyInserted" type="text" value="${moneyInserted}">
		</p>
		<p>
			<label for="amountInserted">Amount Inserted</label>
			<input id="amountInserted" type="text" value="${amountInserted}">
		</p>
		<p>
			<label for="output">Machine Output</label>
			<input id="output" type="text" value="${output}">
		</p>
	</div>
	
	<div class="admin">
		<form method="post" action="<c:url value="/serviceVendingMachine.html"/>">
			<input type="hidden" name="vendingMachine" value="${index}">
			<input type="button" id="service" name="service" value="Service Machine" onClick="this.form.submit()">
		</form>
	</div>
	
	<div class="payment">
		<div class="coinButton">
			<form method="post" action="<c:url value="/insertMoney.html"/>">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="hidden" name="money" value="${nickle}">
				<input type="button" id="nickle" name="nickle" value="Nickle" onClick="this.form.submit()">
			</form>
		</div>
		<div class="coinButton">
			<form method="post" action="<c:url value="/insertMoney.html"/>">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="hidden" name="money" value="${dime}">
				<input type="button" id="dime" name="dime" value="Dime" onClick="this.form.submit()">
			</form>
		</div>
		<div class="coinButton">
			<form method="post" action="<c:url value="/insertMoney.html"/>">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="hidden" name="money" value="${quarter}">
				<input type="button" id="quarter" name="quarter" value="Quarter" onClick="this.form.submit()">
			</form>
		</div>
		<div class="coinButton">
			<form method="post" action="<c:url value="/insertMoney.html"/>">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="hidden" name="money" value="${dollar}">
				<input type="button" id="dollar" name="dollar" value="Dollar" onClick="this.form.submit()">
			</form>		
		</div>
		<div class="coinButton">
			<form method="post" action="<c:url value="/coinReturn.html"/>">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="button" id="coinReturn" name="coinReturn" value="Coin Return" onClick="this.form.submit()">
			</form>
		</div>
	</div>
	
	<div class="vendItem">
		<div class="itemButton">
			<form method="post" action="<c:url value="/buyItem.html"/>">
				<input type="hidden" name="item" value="A">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="submit" id="a" name="a" value="Vend A">
			</form>
		</div>
		<div class="itemButton">
			<form method="post" action="<c:url value="/buyItem.html"/>">
				<input type="hidden" name="item" value="B">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="submit" id="b" name="b" value="Vend B">
			</form>
		</div>
		<div class="itemButton">
			<form method="post" action="<c:url value="/buyItem.html"/>">
				<input type="hidden" name="item" value="C">
				<input type="hidden" name="vendingMachine" value="${index}">
				<input type="submit" id="c" name="c" value="Vend C">
			</form>
		</div>
	</div>
	
	
</body>
</html>
