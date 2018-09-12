// 标识当前做的是哪种类型的订单，默认处理的是采购
var type=1;
/**
 * 日期格式化器
 * @param value
 * @returns
 */
function formatDate(value){
	if(value){
		return new Date(value).Format("yyyy-MM-dd");
	}
	return "";
}


/**
 * 订单状态格式化器
 * @param value
 * @returns
 */
function formatState(value){
	if(type == 1){
		switch (value * 1) {
			case 0:return '未审核';
			case 1:return '已审核';
			case 2:return '已确认';
			case 3:return '已入库';
			default: return '';
		}
	}
	if(type == 2){
		switch (value * 1) {
			case 0:return '未出库';
			case 1:return '已出库';
			default: return '';
		}
	}
}

/**
 * 订单明细状态格式化器
 * @param value
 * @returns
 */
function formatDetailState(value){
	if(type == 1){
		switch (value * 1) {
			case 0:return '未入库';
			case 1:return '已入库';
			default: return '';
		}
	}
	if(type == 2){
		switch (value * 1) {
			case 0:return '未出库';
			case 1:return '已出库';
			default: return '';
		}
	}
}

/**
 * 盘盈盘亏类型格式化器
 * @param value
 * @returns {string}
 */
function formatInventoryType(value) {
    return value == 0 ? "盘亏" : "盘盈";
}

/**
 * 盘盈盘亏状态格式化器
 * @param value
 * @returns {string}
 */
function formatInventoryState(value) {
    return value == 0 ? "未审核" : "已审核";
}

/**
 * 库存操作类型格式化器
 * @param value
 * @returns
 */
function formatOperType(value) {
	switch (value * 1) {
		case 1:return '入库';
		case 2:return '出库';
		default: return '';
	}
}

/**
 * 退货订单状态格式化器
 */
function formatReturnState(value){
	if(type == 1){
		switch (value * 1) {
			case 0:return '未审核';
			case 1:return '已审核';
			case 2:return '已出库';
			default: return '';
		}
	}
	if(type == 2){
		switch (value * 1) {
			case 0:return '未审核';
			case 1:return '已审核';
			case 2:return '已入库';
			default: return '';
		}
	}
}

/**
 * 退货订单明细状态格式化器
 */
function formatReturnDetailState(value){
	if(type == 2){
		switch (value * 1) {
			case 0:return '未入库';
			case 1:return '已入库';
			default: return '';
		}
	}
	if(type == 1){
		switch (value * 1) {
			case 0:return '未出库';
			case 1:return '已出库';
			default: return '';
		}
	}
}