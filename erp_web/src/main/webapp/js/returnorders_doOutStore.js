$(function(){
	//采购退货订单列表
	$('#grid').datagrid({
		url:'returnorders!listByPage.action?t1.type=1&t1.state=1',
		singleSelect:true,
		pagination:true,
		title:'采购退货订单列表',
		columns:[[
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'录入日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			{field:'endtime',title:'出库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'checkerName',title:'审查员',width:100},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'供应商',width:100},
			{field:'totalmoney',title:'总金额',width:100},
			{field:'state',title:'订单状态',width:100,formatter:formatReturnState},
			{field:'waybillsn',title:'运单号',width:100}
		          ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	$('#ordersDlg').dialog('open');
		    	$('#uuid').html(rowData.uuid);
				$('#supplierName').html(rowData.supplierName);
				$('#state').html(formatReturnState(rowData.state));
				$('#createrName').html(rowData.createrName);
				$('#checkerName').html(rowData.checkerName);
				$('#enderName').html(rowData.enderName);
				$('#createtime').html(formatDate(rowData.createtime));
				$('#checktime').html(formatDate(rowData.checktime));
				$('#endtime').html(formatDate(rowData.endtime));
				$('#itemgrid').datagrid('loadData',rowData.returnOrderDetails);
		    }
	});
	
	//订单详情窗口
	$('#ordersDlg').dialog({
		title:'订单详情',
		width:700,
		height:340,
		closed:true,
		modal:true
	});
	
	//详情中的明细表格
	$('#itemgrid').datagrid({
		singleSelect:true,
		title:'商品列表',
		columns:[[
			{field:'uuid',title:'编号',width:60},
			{field:'goodsuuid',title:'商品编号',width:80},
			{field:'goodsname',title:'商品名称',width:100},
			{field:'price',title:'价格',width:100},
			{field:'num',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:formatReturnDetailState}
		          ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	$('#itemDlg').dialog('open');
				$('#goodsuuid').html(rowData.goodsuuid);
				$('#goodsname').html(rowData.goodsname);
				$('#num').html(rowData.num);
				$('#id').val(rowData.uuid);
		    }
	})
	
	//出库窗口初始化
	$('#itemDlg').dialog({
		title:'出库',
		width:300,
		height:200,
		modal:true,
		closed:true,
		buttons:[{
			text:'出库',
			iconCls:'icon-save',
			handler:doOutStore
		}]
	});
});

function doOutStore(){
	$.messager.confirm('确认', '确认要出库吗', function(yes) {
		if(yes){
			var submitdata = $('#itemForm').serializeJSON();
			if(submitdata.storeuuid == ''){
				$.messager.alert('提示','请选择仓库','info');
				return;
			}
			$.ajax({
				url:'returnorderdetail!doOurStore.action',
				data:submitdata,
				dataType:'json',
				type:'post',
				success:function(rtn){
					$.messager.alert('提示', rtn.message, 'info', function(){
						if(rtn.success){
							$('#itemDlg').dialog('close');
							var row = $('#itemgrid').datagrid('getSelected');
							row.state = '1';
							var data = $('#itemgrid').datagrid('getData');
							$('#itemgrid').datagrid('loadData',data);
							var flag = true;
							$.each(data.rows,function(i,r){
								if(r.state * 1 == 0){
									flag = false;
									return false;
								}
							});
							if(flag){
								$('#ordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							}
						}
					});
				}
			});
		}
	});
}