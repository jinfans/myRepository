$(function () {
    $("#grid").datagrid({
        title:'盘盈盘亏审核',
        url: 'inventory!listByPage.action?t1.state=0',
        singleSelect: true,
        pagination: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'goodsName', title: '商品', width: 100},
            {field: 'storeName', title: '仓库', width: 100},
            {field: 'num', title: '数量', width: 100},
            {field: 'type', title: '类型', width: 100, formatter: formatInventoryType},
            {field: 'createtime', title: '登记日期', width: 100, formatter: formatDate},
            {field: 'checktime', title: '审核日期', width: 100, formatter: formatDate},
            {field: 'createrName', title: '登记人', width: 100},
            {field: 'checkerName', title: '审核人', width: 100},
            {field: 'state', title: '状态', width: 100, formatter: formatInventoryState},
            {field: 'remark', title: '备注', width: 100}
        ]],
        onDblClickRow:function(rowIndex, rowData){
        	$('#inventoryDlg').dialog('open');
			
			/**
			 * 编号
			 * 登记日期
			 * 商品
			 * 仓库
			 * 数量
			 * 类型
			 * 备注
			 */
			$('#uuid').html(rowData.uuid);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#goodsName').html(rowData.goodsName);
			$('#storeName').html(rowData.storeName);
			$('#num').html(rowData.num);
			$('#type').html(formatInventoryType(rowData.type));
			$('#remark').html(rowData.remark);
			
		}
	});

    $("#inventoryDlg").dialog({
        title: '盘盈盘亏审核',
        width: 280,
        height: 210,
        closed: true,
        modal: true,
        buttons: [
            {
                text: '审核',
                iconCls: 'icon-search',
                handler: doCheck
            }
        ]
    });
})

/**
 * 订单审核
 * @returns
 */
function doCheck(){
	$.messager.confirm('确认', '确认要审核吗?', function(yes) {
		if (yes) {
			$.ajax({
				url : 'inventory!doCheck.action',
				data : {id:$('#uuid').html()},// 订单编号
				dataType : 'json',
				type : 'post',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭详情窗口
							$('#inventoryDlg').dialog('close');
							// 刷新订单列表
							$('#grid').datagrid('reload');
						}
					});
				}
			});
		}
	});
}
