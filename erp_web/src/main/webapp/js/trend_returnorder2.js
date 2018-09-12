$(function() {
	$('#grid').datagrid({
		title:'销售统计列表',
		url : 'report!returnorderTrend.action',
		singleSelect : true,
		columns : [ [ 
			{field : 'name',title : '商品类型',width : 100}, 
			{field : 'y',title : '销售额',width : 100}
		] ],
		onLoadSuccess:function(data){
			//在数据加载成功的时候触发。
			//alert(JSON.stringify(data));
			showChart(data.rows);
		}
	});
	
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		if(formData.endDate != ''){
			// 结束日期有值，补23:59:59
			formData.endDate+=" 23:59:59.999";
		}
		$('#grid').datagrid('reload',formData);
	});
	
	
});

function showChart(_data){
	var month=[];
	var money=[];
	$.each(_data,function(i,d){
		//d {};
		d.drilldown=true;
		month.push(_data.name)
		money.push(_data.y)
	});
	$('#container').highcharts();
	// Create the chart
	$('#container').highcharts({
		chart: {
			type: 'column',
			events: {
				drillup: function(e) {
					// 上钻回调事件
					console.log(e.seriesOptions);
				},
				drilldown: function (e) {
					if (!e.seriesOptions) {
						var chart = this;
						//alert(JSON.stringify(e.point.name));//e.point是点击图的数据
						var name = e.point.month;
						var formData = $('#searchForm').serializeJSON();
						
						formData.month=e.point.name;
						
						//alert(JSON.stringify(formData))
						chart.showLoading('正在努力加载中 ...');
						$.ajax({
							url : 'report!returnorderTrend2.action',
							data : formData,
							dataType : 'json',
							type : 'post',
							success : function(rtn) {
								var obj = {};
								obj.data=rtn;
								obj.name=rtn.name;
								chart.hideLoading();
								chart.addSeriesAsDrilldown(e.point, obj);
							}
						});
					}
				}
			}
		},
		title: {
			text: 'Async drilldown'
		},
		 xAxis: {
	        	categories: month,
	            crosshair: true
        },
        yAxis: {
            title: {
            	 text: '退货金额 (元)'
            }
        },
		legend: {
			enabled: false
		},
		plotOptions: {
			series: {
				borderWidth: 0,
				dataLabels: {
					enabled: true
				}
			}
		},
		series: [{
			name: 'Things',
			colorByPoint: true,
			data: _data
		}],
		drilldown: {
			series: []
		}
	});
	
}