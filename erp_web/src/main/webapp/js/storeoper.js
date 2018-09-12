$(function() {
	$('#grid').datagrid({
		title:'库存变更记录列表',
		rownumbers:true,
		url : 'storeoper!listByPage.action',
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'empName',title:'操作员工',width:100},
			{field:'opertime',title:'操作日期',width:140,formatter:function(value){
				return new Date(value).Format('yyyy-MM-dd hh:mm');
			}},
			{field:'storeName',title:'仓库',width:100},
			{field:'goodsName',title:'商品',width:100},
			{field:'num',title:'数量',width:100},
			{field:'type',title:'操作类型',width:100,formatter:formatOperType}
		] ],
		onDblClickRow: function(rowIndex, rowData){
			$('#storeoperDlg').dialog('open');
			
			$('#storeName').html(rowData.storeName);
			$('#goodsName').html(rowData.goodsName);
			
			$('#itemgrid').datagrid('load', {'t1.goodsuuid': rowData.goodsuuid});
			
		}
	});
	
	$('#storeoperDlg').dialog({
		title:'订单详情',
		width:700,
		height:340,
		closed:true,
		modal:true
	});
	
	$('#itemgrid').datagrid({
		title:'库存操作列表',
		rownumbers:true,
		url:'storeoper!listByPage.action',
		singleSelect : true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:60},
			{field:'empuuid',title:'操作者编号',width:60},
			{field:'empName',title:'操作者姓名',width:80},
			{field:'goodsuuid',title:'商品编号',width:60},
			{field:'goodsName',title:'商品名称',width:100},
			{field:'num',title:'数量',width:40},
			{field:'opertime',title:'操作时间',width:100,formatter:formatDate},
			{field:'storeuuid',title:'仓库编号',width:60},
			{field:'storeName',title:'仓库名称',width:100},
			{field:'type',title:'类型',width:60,formatter:formatOperType}
		] ]
	});
	
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('reload',formData);
	});
});