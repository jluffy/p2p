<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- html <head>标签部分  -->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>蓝源Eloan-P2P平台(系统管理平台)</title>
	<#include "../common/header.ftl"/>
	<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
	
	<script type="text/javascript">
		$(function(){
		    //分页显示systemDictionaryItem列表
			$('#pagination').twbsPagination({
				totalPages : ${pageResult.totalPage}||1,//不能使单|  会进行或运算得到页数不对
				startPage : ${pageResult.currentPage},
				visiblePages : 5,
				first : "首页",
				prev : "上一页",
				next : "下一页",
				last : "最后一页",
				onPageClick : function(event, page) {
					$("#currentPage").val(page);
					$("#searchForm").submit();
				}
			});
			//
			//监听展示的systemDictionary选项,点击时提交表单 携带parentId;
			$(".group_item").click(function(){
                $("#currentPage").val(1);//查询时从第一页开始  要不在翻页到后面再单查的时候可能会报错说 startpage不对Error: Start page option is incorrect
                //获取到当前所点击的数据字典的id
				var parentId=$(this).data("dataid");
				//alert(parentId);
				$("#parentId").val(parentId);
			    //找到整个表单提交
				$("#searchForm").submit();
			});
			//
			//为选中的元素做高亮显示
			var parentId='${(qo.parentId)!""}'//每次点击选项元素时后台会拿到qo 也会共享到前台
			//根据parentid找到该元素做高亮显示 active
			if(parentId){
			    $(".group_item[data-dataid="+parentId+"]").closest("li").addClass("active");
			}
			//
			//添加明细
			$("#addSystemDictionaryItemBtn").click( function(){
                if(!parentId){
                    $.messager.popup("请选择需要添加数据明细的类型");
                    return;
				}
				$("#systemDictionaryItemModal").modal("show");
			});
			//监听保存
			$("#saveBtn").click(function(){
			   	 //点击保存时候要判断是否已经选择父级选项 没有则不执行 parentid上述已经拿到
				    $("#editFormParentId").val(parentId);
					$("#editForm").ajaxSubmit({
                        dataType:'json',
                        success:function (data) {
                            if(data.success){
                                $("#currentPage").val(1);
                                $("#searchForm").submit();
                            }else{
                                $.messager.popup(data.msg);
                            }
                        }
					})

			});
			//
			//修改数据表单
			$(".edit_Btn").click(function(){
                if(!parentId){
                    $.messager.popup("请选择需要添加数据明细的类型");
                    return;
                }
                $("#searchForm").clearForm(true);
			    var data=$(".edit_Btn").data("json");
                console.log(data);
				//设置表单里的parentId
				$("#editFormParentId").val(data.parentId);
				$("#systemDictionaryItemId").val(data.id);
				$("#sequence").val(data.sequence);
				$("#title").val(data.title);
                $("#systemDictionaryItemModal").modal("show");
            })

		});
		//
		//高级查询
		//无需写 提交表单就会带出  button标签默认类型为submit
		//
		</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="systemDictionaryItem" />
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>数据字典明细管理</h3>
				</div>
				<div class="col-sm-12">
					<!-- 提交分页的表单 -->
					<form id="searchForm" class="form-inline" method="post" action="/systemDictionaryItem_list">
						<input type="hidden" id="currentPage" name="currentPage" value="${(qo.currentPage)!1}"/>
						<input type="hidden" id="parentId" name="parentId" value='${(qo.parentId)!""}' />
						<div class="form-group">
						    <label>关键字</label>
						    <input class="form-control" type="text" name="keyword" value="${(qo.keyword!'')}">
						</div>
						<div class="form-group">
							<button id="query"  class="btn btn-success"><i class="icon-search"></i>查询</button><#--button标签默认就是submit-->
							<a href="javascript:void(-1);" class="btn btn-success" id="addSystemDictionaryItemBtn">添加数据字典明细</a>
						</div>
					</form>
					<div class="row"  style="margin-top:20px;">
						<div class="col-sm-3">
							<ul id="menu" class="list-group">
								<li class="list-group-item">
									<a href="#" data-toggle="collapse" data-target="#systemDictionary_group_detail"><span>数据字典分组</span></a>
									<ul class="in" id="systemDictionary_group_detail">
										<#list systemDictionaryGroups as vo><!-- id="pg_${vo.id}" -->
										   <li><a class="group_item"  data-dataid="${vo.id}" href="javascript:;"><span>${vo.title}</span></a></li>
										</#list>
									</ul>
								</li>
							</ul>
						</div>
						<div class="col-sm-9">
							<table class="table">
								<thead>
									<tr>
										<th>名称</th>
										<th>序列</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								<#list pageResult.listData as vo>
									<tr>
										<td>${vo.title}</td>
										<td>${vo.sequence!""}</td>
										<td>
											<a href="javascript:void(-1);" class="edit_Btn" data-json='${vo.jsonString}'>修改</a> &nbsp;
										</td>
									</tr>
								</#list>
								</tbody>
							</table>
							
							<div style="text-align: center;">
								<ul id="pagination" class="pagination"></ul>
							</div>
				
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="systemDictionaryItemModal" class="modal" tabindex="-1" role="dialog">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">编辑/增加</h4>
	      </div>
	      <div class="modal-body">
	       	  <form id="editForm" class="form-horizontal" method="post" action="systemDictionaryItem_update" style="margin: -3px 118px">
				    <input id="systemDictionaryItemId" type="hidden" name="id" value="" />
			    	<input type="hidden" id="editFormParentId" name="parentId" value="" />
				   	<div class="form-group">
					    <label class="col-sm-3 control-label">名称</label>
					    <div class="col-sm-6">
					    	<input type="text" class="form-control" id="title" name="title" placeholder="字典值名称">
					    </div>
					</div>
					<div class="form-group">
					    <label class="col-sm-3 control-label">顺序</label>
					    <div class="col-sm-6">
					    	<input type="text" class="form-control" id="sequence" name="sequence" placeholder="字典值显示顺序">
					    </div>
					</div>
			   </form>
		  </div>
	      <div class="modal-footer">
	      	<a href="javascript:void(0);" class="btn btn-success" id="saveBtn" aria-hidden="true">保存</a>
		    <a href="javascript:void(0);" class="btn" data-dismiss="modal" aria-hidden="true">关闭</a>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>