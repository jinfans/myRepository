$(function () {
    /**
     * 采购退货列表配置
     */
    $('#grid').datagrid({
        title: '采购退货订单列表',
        url: 'returnorders!myListByPage.action?t1.type=1', // 列出我的采购退货订单
        singleSelect: true,
        pagination: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'createtime', title: '录入日期', width: 100, formatter: formatDate},
            {field: 'checktime', title: '审核日期', width: 100, formatter: formatDate},
            {field: 'endtime', title: '出库日期', width: 100, formatter: formatDate},
            {field: 'createrName', title: '下单员', width: 100},
            {field: 'checkerName', title: '审查员', width: 100},
            {field: 'enderName', title: '库管员', width: 100},
            {field: 'supplierName', title: '供应商', width: 100},
            {field: 'totalmoney', title: '总金额', width: 100},
            {field: 'state', title: '订单状态', width: 100, formatter: formatState},
            {field: 'waybillsn', title: '运单号', width: 100}
        ]],
        onDblClickRow: function (rowIndex, rowData) {
            // 弹出详情窗口
            $('#ordersDlg').dialog('open');

            // 弹出详情窗口
            $('#ordersDlg').dialog('open');
            $('#uuid').html(rowData.uuid);
            $('#supplierName').html(rowData.supplierName);
            $('#state').html(formatState(rowData.state));
            $('#createrName').html(rowData.createrName);
            $('#checkerName').html(rowData.checkerName);
            $('#starterName').html(rowData.starterName);
            $('#enderName').html(rowData.enderName);
            $('#createtime').html(formatDate(rowData.createtime));
            $('#checktime').html(formatDate(rowData.checktime));
            $('#starttime').html(formatDate(rowData.starttime));
            $('#endtime').html(formatDate(rowData.endtime));

            // 加载明细的数据
            $('#itemgrid').datagrid('loadData', rowData.returnOrderDetails);
        },
        toolbar: [
            {
                text: '采购退货登记',
                iconCls: 'icon-add',
                handler: function () {
                    // 弹出采购退货登记的窗口
                    $('#addOrdersDlg').dialog('open');
                }
            }
        ]
    });

    // 订单详情窗口初始化
    $('#ordersDlg').dialog({
        title: '订单详情',
        width: 700,
        height: 340,
        closed: true,
        modal: true
    });

    //订单明细表格初始化
    $('#itemgrid').datagrid({
        title: '商品列表',
        singleSelect: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 60},
            {field: 'goodsuuid', title: '商品编号', width: 80},
            {field: 'goodsname', title: '商品名称', width: 100},
            {field: 'price', title: '价格', width: 100},
            {field: 'num', title: '数量', width: 100},
            {field: 'money', title: '金额', width: 100},
            {field: 'state', title: '状态', width: 60, formatter: formatDetailState}
        ]]
    });

    // 采购退货申请窗口初始化
    $('#addOrdersDlg').dialog({
        title: '采购退货申请',
        width: 700,
        height: 400,
        closed: true,
        modal: true
    });
});