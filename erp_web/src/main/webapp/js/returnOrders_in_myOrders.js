$(function(){
	// 订单列表
	$('#grid').datagrid({
		title:'销售订单退货录入',
		url : 'returnorders!myListByPage.action?t1.type=2&t1.state=0', // 列出销售订单退货
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'录入日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			{field:'endtime',title:'入库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'checkerName',title:'审核员',width:100},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'客户',width:100},
			{field:'totalmoney',title:'合计金额',width:100},
			{field:'state',title:'订单状态',width:100,formatter:formatState},
			{field:'waybillsn',title:'运单号',width:100}
		] ],
			onDblClickRow:function(rowIndex, rowData){//双击事件
			 //弹出窗口		rowIndex：点击的行的索引值，该索引值从0开始。
			 //				rowData：对应于点击行的记录。
			 $('#ordersDlg').dialog('open');
			 
			 	$('#uuid').html(rowData.uuid);
				$('#supplierName').html(rowData.supplierName);
				$('#state').html(formatState(rowData.state));
				$('#createrName').html(rowData.createrName);
				$('#checkerName').html(rowData.checkerName);
				$('#enderName').html(rowData.enderName);
				$('#createtime').html(formatDate(rowData.createtime));
				$('#checktime').html(formatDate(rowData.checktime));
				$('#endtime').html(formatDate(rowData.endtime));
				
			// 加载明细的数据
		$('#itemgrid').datagrid('loadData',rowData.returnOrderDetails);
		
		},toolbar:[
				{
					text : '销售订单退货登记',
					iconCls : 'icon-add',
					hegiht:300,
					handler : function() {
						// 弹出增加订单详情的窗口
						$('#addOrdersDlg').dialog('open');
					}
				}
			]
	});

	// 订单详情窗口初始化
	$('#ordersDlg').dialog({
		title:'订单详情',
		width:700,
		height:340,
		closed:true,
		modal:true
	});
	
	//明细表格初始化
	$('#itemgrid').datagrid({
		title:'商品列表',
		singleSelect : true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'goodsuuid',title:'商品编号',width:100},
			{field:'goodsname',title:'商品名称',width:100},
			{field:'price',title:'价格',width:100},
			{field:'num',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:formatDetailState}
		 ] ]
		 
		 
	});
	
	
	// 销售订单登记窗口初始化
	$('#addOrdersDlg').dialog({
		title:'增加订单',
		width:700,
		height:400,
		closed:true,
		modal:true
	});
	
});

/**
 * 订单状态格式化器
 */
function formatState(value){
	//采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库
	switch (value * 1) {
	case 0:
		return '未审核';
	case 1:
		return '已审核';
	case 2:
		return '已入库';
	default:
		return '';
	}
	
}
/**
 * 订单明细状态格式化器
 */
function formatDetailState(value){
	//采购：0=未入库，1=已入库  销售：0=未出库，1=已出库
	switch (value * 1) {
	case 0:
		return '未入库';
	case 1:
		return '已入库';
	default:
		return '';
	}
	
}