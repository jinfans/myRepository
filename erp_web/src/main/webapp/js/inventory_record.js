$(function () {
    $("#grid").datagrid({
        title:'盘盈盘亏登记',
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
        toolbar: [
            {
                text: '盘盈盘亏登记',
                iconCls: 'icon-add',
                handler: function () {
                    $('#editDlg').dialog('open');
                    $('#editForm').form("clear");
                }
            }
        ]
    });

    $("#editDlg").dialog({
        title: '盘盈盘亏登记',
        width: 280,
        height: 240,
        closed: true,
        modal: true,
        buttons: [
            {
                text: '登记',
                iconCls: 'icon-save',
                handler: doCreate
            }
        ]
    });

    var storeuuid = null;
    $('#store').combobox({
        url:'store!list.action',
        textField:'name',
        valueField:'uuid',
        onSelect: function (record) {
            storeuuid = record["uuid"];
            $('#goods').combobox({
                url:'storedetail!goodsByStore.action?t1.storeuuid=' + storeuuid,
                textField:'name',
                valueField:'uuid'
            });
        }
    });
});

function doCreate() {
    $.messager.confirm('温馨提示....', '确认要登记吗', function(yes) {
        if (yes) {
            var submitData = $('#editForm').serializeJSON();
            if (submitData["t1.goodsuuid"] == '') {
                $.messager.alert('温馨提示....', '请选择商品', 'info');
                return;
            }
            if (submitData["t1.storeuuid"] == '') {
                $.messager.alert('温馨提示....', '请选择仓库', 'info');
                return;
            }
            if (submitData["t1.num"] == '') {
                $.messager.alert('温馨提示....', '请填写数量', 'info');
                return;
            }
            if (submitData["t1.type"] == undefined) {
                $.messager.alert('温馨提示....', '请选择登记类型', 'info');
                return;
            }
            if (submitData["t1.remark"] == '') {
                $.messager.alert('温馨提示....', '请填写完备注', 'info');
                return;
            }
            $.ajax({
                url: 'inventory!doCreate.action',
                data: submitData,
                dataType: 'json',
                type: 'post',
                success: function (rtn) {
                    $.messager.alert('提示', rtn.message, 'info', function() {
                        if (rtn.success) {
                            // 关闭详情窗口
                            $('#editDlg').dialog('close');
                            // 刷新订单列表
                            $('#grid').datagrid('reload');
                        }
                    });
                }
            })
        }
    })
}