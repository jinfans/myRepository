var chart='';
$(function () {
	$('#grid').datagrid({
		url:'report!returnorderTrend.action',
		singleSelect: true,
		columns : [ [ 
			{field : 'month',title : '商品类别',width : 100}, 
			{field : 'y',title : '销售额',width : 100}, 
		] ],
		onLoadSuccess:function(allData){
			showChart(allData.rows);
		}
	});
	
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	
	showChart(0);
	function showValues() {
        $('#R0-value').html(chart.options.chart.options3d.alpha);
        $('#R1-value').html(chart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R0').on('change', function () {
        chart.options.chart.options3d.alpha = this.value;
        showValues();
        chart.redraw(false);
    });
    $('#R1').on('change', function () {
        chart.options.chart.options3d.beta = this.value;
        showValues();
        chart.redraw(false);
    });

    showValues();
	
})

function showChart(allData){
	var month=[];
	var money=[];
	$.each(allData,function(index,data){
		month.push(data.month);
		money.push(data.y)
	})
	chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            type: 'column',
            margin: 75,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
        },
        title: {
        	 text: '年度销售退货趋势分析图'
        },
        subtitle: {
            text: '每月退货金额'
        },
        plotOptions: {
        	series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.1f}'
                }
            }
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
        credits:{
        	enable:true,
        	href:'index.html',
        	style:'',//样式
        	text:'数据来源'//内容
        },
        series: [{
        	colorByPoint: true,
            name: '退货金额',
            data: money
        }]
    });
}