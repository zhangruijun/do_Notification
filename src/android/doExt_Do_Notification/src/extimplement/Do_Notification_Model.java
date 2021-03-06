package extimplement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import core.helper.jsonparse.DoJsonNode;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import extdefine.Do_Notification_IMethod;

/**
 * 自定义扩展API组件Model实现，继承DoSingletonModule抽象类，并实现Do_Notification_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象；
 * 获取DoInvokeResult对象方式new DoInvokeResult();
 */
public class Do_Notification_Model extends DoSingletonModule implements Do_Notification_IMethod{

	public Do_Notification_Model() throws Exception {
		super();
	}
	
	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, DoJsonNode _dictParas,
			DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult)
			throws Exception {
		//...do something
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}
	
	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用，
	 * 可以根据_methodName调用相应的接口实现方法；
	 * @throws Exception 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名
	 * #如何执行异步方法回调？可以通过如下方法：
	 * _scriptEngine.callback(_callbackFuncName, _invokeResult);
	 * 参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 * 获取DoInvokeResult对象方式new DoInvokeResult();
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, DoJsonNode _dictParas,
			DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		if ("toast".equals(_methodName)) {
			toast(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		}
		if ("alert".equals(_methodName)) {
			alert(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		}
		if ("confirm".equals(_methodName)) {
			this.confirm(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		}
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 弹出alert窗口；
	 * @throws Exception 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void alert(DoJsonNode _dictParas, final DoIScriptEngine _scriptEngine,final String _callbackFuncName) throws Exception {
		String _title = _dictParas.getOneText("title", "");
		String _content = _dictParas.getOneText("text", "");
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		final AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
		builder.setMessage(_content).setTitle(_title).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				_scriptEngine.callback(_callbackFuncName, new DoInvokeResult());
			}
		});
		_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	/**
	 * 弹出confirm窗口；
	 * @throws Exception 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void confirm(DoJsonNode _dictParas, final DoIScriptEngine _scriptEngine, final String _callbackFuncName) throws Exception {
		String _title = _dictParas.getOneText("title", "");
		String _content = _dictParas.getOneText("text", "");
		String _button1text = _dictParas.getOneText("leftbuttontext", "确定");
		String _button2text = _dictParas.getOneText("rightbuttontext", "取消");
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		final AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
		final DoInvokeResult _invokeResult = new DoInvokeResult();
		builder.setMessage(_content).setTitle(_title).setCancelable(false).setPositiveButton(_button1text, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					DoJsonNode _node = new DoJsonNode();
					_node.setOneInteger("index", 1);
					_invokeResult.setResultNode(_node);
					_scriptEngine.callback(_callbackFuncName, _invokeResult);
				} catch (Exception e) {
					throw new RuntimeException("confirm", e);
				}
			}
		}).setNeutralButton(_button2text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					DoJsonNode _node = new DoJsonNode();
					_node.setOneInteger("index", 2);
					_invokeResult.setResultNode(_node);
					_scriptEngine.callback(_callbackFuncName, _invokeResult);
				} catch (Exception e) {
					throw new RuntimeException("confirm", e);
				}
			}
		});
		_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	/**
	 * 弹出toast窗口；
	 * @throws Exception 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void toast(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine,String _callbackFuncName) throws Exception {
		final String _text = _dictParas.getOneText("text", "");
		final Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				android.widget.Toast.makeText(_activity, _text, android.widget.Toast.LENGTH_SHORT).show();
			}
		});
	}
}