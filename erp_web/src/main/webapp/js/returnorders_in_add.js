//existEditIndex：用来保存当前进入编辑状态的行的索引，初始值为-1，表示当前没有行进入编辑状态。
var existEditIndex = -1;

$(function(){
	//数据来源于前端，不是后台，所以不用url
	$('#addOrdersGrid').datagrid({
		singleSelect:true,			//如果为true，则只允许选择一行。
		showFooter:true,			//定义是否显示行脚。
		columns:[[    
					{field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{disabled:true}}},
					{field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
						url:'goods!list.action',valueField:'name',textField:'name',
						onSelect:function(record){
							// onSelect方法在用户选择列表项的时候触发。
							// record选中的商品信息 一行数据
							//alert(JSON.stringify(record));
							
							//价格
							var price = record.outprice;		//销售价
							//获取该行的编辑器(价格编辑器)
							var priceEditor = $('#addOrdersGrid').datagrid('getEditor',{index:existEditIndex,field:'price'});
							// 设置编辑器上输入框的值
							$(priceEditor.target).val(price);
							
							//商品编号
							var goodsuuid = record.uuid;		//销售价
							//获取该行的编辑器(价格编辑器)
							var priceEditor = getEditor("goodsuuid");
							// 设置编辑器上输入框的值
							$(priceEditor.target).val(goodsuuid);
							
							// 数量的编辑器
							var numEditor = getEditor("num");
							// 选中数量输入框
							$(numEditor.target).select();
							
							//计算金额
							cal();
							
							//计算合计金额
							sum();

						}
					}}},
					{field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{min:0,precision:2,disabled:true}}},	//disabled:是否禁用该字段
					{field:'num',title:'数量',width:100,editor:{type:'numberbox',options:{min:0}}},	//editor:指定编辑类型numberbox,为数字
					{field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{min:0,precision:2,disabled:true}}},
					{field:'--',title:'操作',width:100,formatter:function(value,rowData,rowIndex){
						if(rowData.num=='合计'){
							return ;
						}
						return '<a href= "javascript:void(0);" onclick= "deleteRow('+ rowIndex +')">删除</a>';
					}}
		      ]],
		      toolbar: [{	//toolbar：顶部工具栏的DataGrid面板 
				  		iconCls: 'icon-add',
				  		text:'增加',
				  		handler: function(){
				  			if(existEditIndex > -1){
				  			// 存在编辑的行, 则把它关闭，数据就会进入表格中
				  				$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
				  			}
				  			//使用appendRow：给表格动态追加一行
				  			$('#addOrdersGrid').datagrid('appendRow',{
				  				goodsuuid:'',
				  				goodsname:'',
				  				price:'',
				  				num: 0,
				  				money: 0
				  			});
				  			
				  			// 如何获取所有行数(getRows返回当前页的所有行。)
				  			var rows = $('#addOrdersGrid').datagrid('getRows');		//返回当前页的所有行
				  			//获取当前行
				  			var index = rows.length - 1;
				  			// 开启编辑
				  			$('#addOrdersGrid').datagrid('beginEdit',index);
				  			existEditIndex = index;
				  			//开启编辑的时候绑定自动计算金额事件
				  			bindGridEven();
				  		}
				  	},{
				  		iconCls: 'icon-save',
				  		text:'提交',
				  		handler: function(){
				  			var submitData = $('#orderForm').serializeJSON();
				  			if(submitData['t.supplieruuid'] == ''){
				  				$.messager.alert('提示','请选择供应商','info');
				  				return ;
				  			}
				  			//关闭正在编辑的行
				  			if(existEditIndex > -1){
				  				// 存在编辑的行, 则把它关闭，数据就会进入表格中
				  				$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
				  			}
				  			//明细的数据
				  			var rows = $('#addOrdersGrid').datagrid('getRows');
				  			// 把所有的明细转成json格式的字符串
				  			var json = JSON.stringify(rows);
				  			submitData.json = json;
				  			$.ajax({
				  				url:'returnorders!salesEntry.action',//sell销售 
				  				data:submitData,
				  				dataType:'json',
				  				type:'post',
				  				success:function(rtn){
				  					$.messager.alert('提示', rtn.message, 'info',function(){
										if(rtn.success){
											// 清空供应商
											$('#supplier').combogrid('clear');
											// 加载空的数据达到清空的效果
											$('#addOrdersGrid').datagrid('loadData',{total:0,rows:[],footer:[{num:0,money:0}]});
											// 关闭采购申请窗口
											$('#addOrdersDlg').dialog('close');
											// 刷新订单列表
											$('#grid').datagrid('reload');
										}
									});
				  				}
				  			})
				  		}
				  	}],
				  	onClickRow:function(rowIndex, rowData){//先关闭，后开启
						// rowindex 点击的行的下标,
						// rowData, 行的数据
						// 存在编辑的行, 则把它关闭，数据就会进入表格中
						$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
						// 开启编辑
						$('#addOrdersGrid').datagrid('beginEdit',rowIndex);
						// 此时编辑的行下标
						existEditIndex = rowIndex;
						// 绑定自动计算事件
						bindGridEven();
					} 
	});
	
	//更新行脚，并载入数据
	$('#addOrdersGrid').datagrid('reloadFooter',[
		  	{num: '合计', money: 0}
		  ]);
	
	//客户下拉表
	$('#supplier').combogrid({    
	    panelWidth:750,    //面板的宽度
	    idField:'uuid',    //要提交的数据
	    textField:'name',    
	    url:'supplier!list.action?t1.type=2',  
	    mode:'remote',	//用户输入将会自动发送到名为'q'的http请求参数，向服务器检索新的数据
	    columns:[[    
			{field:'uuid',title:'编号',width:100},
			{field:'name',title:'名称',width:100},
			{field:'address',title:'联系地址',width:150},
			{field:'contact',title:'联系人',width:100},
			{field:'tele',title:'联系电话',width:100},
			{field:'email',title:'邮件地址',width:120}  
	    ]]    
	});  

});

/**
 * 获取编辑器
 * @param fieldName
 * @returns
 */
function getEditor(fieldName){
	return $('#addOrdersGrid').datagrid('getEditor', {index:existEditIndex,field:fieldName});
}

/**
 * 计算商品金额
 */
function cal(){
	// 数量的编辑器
	var numEditor = getEditor("num");
	// 选中数量输入框
	var num = $(numEditor.target).val();
	// 价格编辑器
	var priceEditor = getEditor('price');
	// 设置编辑器上输入框的值
	var price = $(priceEditor.target).val();
	// 算出金额 toFixed(digit) 把数值转成字符串，保留小数后digit位
	var money = (num * price).toFixed(2) ;
	
	//金额的编辑器
	var moneyEditor = getEditor('money');
	$(moneyEditor.target).val(money);
	
	// 让money进入到表格的数据中
	// 获取表格的所有数据
	var rows = $('#addOrdersGrid').datagrid('getRows');// 返回当前页的所有行。 
	// 当前正处于编辑的行
	rows[existEditIndex].money = money;
}

/**
 * 绑定事件
 */
function bindGridEven(){
	//获取数量编辑器
	var numEditor = getEditor('num');
	/*$('#numEditor').bind('keyup',function(){
		
	})*/
	$(numEditor.target).bind('keyup',function(){
		//计算金额
		cal();
		//计算合计金额
		sum();
	})
}

/**
 * 计算合计金额
 */
function sum(){
	var rows = $('#addOrdersGrid').datagrid('getRows');		//返回当前页所有的行
	var totalMoney = 0;
	$.each(rows,function(i,row){
		totalMoney += row.money *1;
	});
	totalMoney = totalMoney.toFixed(2);
//	alert(totalMoney);
	
	//更新行脚，并载入数据
	$('#addOrdersGrid').datagrid('reloadFooter',[
		  	{num: '合计', money: totalMoney}
		  ]);
}

function deleteRow(idx){
	//删除前要先关闭编辑状态，数据才能进入表格中，再次加载数据才不会丢失
	$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
	//关闭编辑后，不存在编辑的行，所以设置existEditIndex=-1
	existEditIndex = -1;
	//删除行
	$('#addOrdersGrid').datagrid('deleteRow',idx);	//deleteRow:删除行
	//获取数据,重新加载
	var data = $('#addOrdersGrid').datagrid('getData');		//返回加载完毕后的数据。
	// 重新加载数据,加载本地数据，旧的行将被移除。
	$('#addOrdersGrid').datagrid('load',data);
	// 重新合计金额
	sum();
}

