package com.aiwac.robotapp.patrobot.utils;


import android.util.Log;

import com.aiwac.robotapp.commonlibrary.bean.WifiInfo;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.JsonException;
import com.aiwac.robotapp.commonlibrary.utils.ImageUtil;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.bean.BaseEntity;
import com.aiwac.robotapp.patrobot.bean.FeedTime;
import com.aiwac.robotapp.patrobot.bean.MessageTransform;
import com.aiwac.robotapp.patrobot.bean.aVDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


/**     对象和json直接的相互转换
 * Created by luwang on 2017/10/23.
 */

public class JsonUtil {


    /*     用户 Json 字符串格式
        String jsonStr =
                {
                "opt" : "insert"
                "user":
                    [{"id":1,"number":"15911112222","name":"zhangsan"}]
                }
     */

    //解析json获取操作 ，如插入，更新等
    public static String parseOpt(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String opt = root.getString(Constant.JSON_OPT);
            LogUtil.d(Constant.JSON_PARSE_SUCCESS + opt);
            return opt;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析businessType 获取事物类型
    public static String parseBusinessType(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析errorCode 获取消息是否成功传递到后台
    public static String parseErrorCode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.RETURN_JSON_ERRORCODE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析errorCode 获取消息是否成功传递到后台
    public static String parseToken(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_TOKEN);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析ErrorDesc
    public static String parseErrorDesc(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析用户登录密码
    public static String parsePWD(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_PASSWORD);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析uuid 获取定时任务消息的UUID属性
    public static String parseTimerUUID(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_UUID);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json result等
    public static boolean parseResult(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String errorCode = root.getString(Constant.WEBSOCKET_TIMER_ERRORCODE);
            return Constant.MESSAGE_ERRORCODE_2000.equals(errorCode);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    /**
     *
     * @param jsonStr  json串
     * @param key
     * @return
     */
    public static String parseByKey(String jsonStr, String key){
        try{
            JSONObject root = new JSONObject(jsonStr);
            return root.getString(key);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //将BaseEntity对象转换成json字符串
    public static String baseEntity2Json(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            //root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //通过id查询挂号信息
    public static String queryRegistgerInfoById(BaseEntity baseEntity, String id){
        JSONObject root = new JSONObject();
        try{
            //root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            root.put(Constant.WEBSOCKET_REGISTER_ID, id);
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //解析json获取用户注册是否成功
    public static boolean isUserRegisterSucess(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String isSuccess = root.getString(Constant.USER_REGISTER_ISSUCCESS);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isSuccess);
            return isSuccess.equals(Constant.USER_REGISTER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取验证码
    public static String parseCheckcode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String checkcode = jsonObject.getString(Constant.USER_REGISTER_CHECKCODE);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + checkcode);

            return checkcode;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json 判断用户是否注册
    public static boolean isUserRegisted(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String isNumberExist = jsonObject.getString(Constant.USER_REGISTER_PHONENUMBER_EXIST);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isNumberExist);

            return isNumberExist.equals(Constant.USER_REGISTER_PHONENUMBER_EXIST_YES);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }




    public static String setBussinessType(String bussinessType){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, bussinessType);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    public static String wifiInfoToJson(WifiInfo wifiInfo){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, "0000");
            root.put("ssid", wifiInfo.getSsid());
            root.put("password",wifiInfo.getPassword());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }




    public static String queryPersonInfo(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            //root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

/*    public static User jsonToPersonInfo(String json){
        User user = new User();
        try{
            JSONObject root = new JSONObject(json);

            return user;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }*/
    //解析消息转发的data json字符串
    public static String parseMessageTransData(String json){
        try {
            JSONObject root = new JSONObject(json);
            return root.getString(Constant.WEBSOCKET_MESSAGE_TRANSFORM);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析消息转发的data里的 commantType json字符串
    public static String parseCommantType(String data){
        try {
            JSONObject root = new JSONObject(data);
            LogUtil.d(root.toString());
            return root.getString(Constant.WEBSOCKET_COMMAND_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }
    //解析1003指令，获取移动方向
    public static String parseDiretction(String data){
        try {
            JSONObject root = new JSONObject(data);
            return root.getString(Constant.WEBSOCKET_COMMAND_MOVE_DIRECTION);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析1004,1005指令，获取视频、音频链接
    public static String parseVideo(String data){
        try {
            JSONObject root = new JSONObject(data);
            return root.getString(Constant.WEBSOCKET_COMMAND_VIDEO_LINK_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }




    //解析json 获取讲座  视音频的详细信息
    public static aVDetail parseAVDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                aVDetail  lectureAVDetail = new  aVDetail();

                lectureAVDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
                lectureAVDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE));
                lectureAVDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureAVDetail.setLink(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_AV_LINK));

                return lectureAVDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }


    //解析Json  获取音视频链接json
    public static MessageTransform parseMessageTransform(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            MessageTransform messageTransform = new MessageTransform();
            messageTransform.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE));
            messageTransform.setClientID(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
            messageTransform.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
            messageTransform.setData(root.getString(Constant.WEBSOCKET_MESSAGE_TRANSFORM));
            messageTransform.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));
            messageTransform.setTime(root.getString(Constant.WEBSOCKET_MESSAGE_TIME));
            return messageTransform;
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }


    }

    //解析Json  获取投食，巡航时间列表
    public static FeedTime parseFeedNavigateTransform(String jsonStr){
        ArrayList<String> times = new ArrayList();
        try{
            JSONObject root = new JSONObject(jsonStr);
            FeedTime feedTime = new FeedTime();
            feedTime.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE));
            feedTime.setClientID(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
            feedTime.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
            feedTime.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));
            feedTime.setTime(root.getString(Constant.WEBSOCKET_MESSAGE_TIME));
            //feedTime.setAutoType(root.getString(Constant.WEBSOCKET_SOCKET_AUTOTYPE));
            JSONArray jsonArray = root.getJSONArray(Constant.WEB_SOCKET_TIME_POINTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                times.add((String) jsonArray.get(i));
            }
            feedTime.setTimePoints(times.toArray(new String[times.size()]));
            return feedTime;
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static String time2Json(String autoType){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_SOCKET_GET_TIME_LIST);
            JSONObject root=BaseEntity2Json(baseEntity);
            root.put(Constant.WEBSOCKET_SOCKET_AUTOTYPE,autoType);
            return root.toString();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //将BaseEntity对象转换成json
    public static JSONObject BaseEntity2Json(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            //LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
}
