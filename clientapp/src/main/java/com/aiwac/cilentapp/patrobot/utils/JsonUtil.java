package com.aiwac.cilentapp.patrobot.utils;



import com.aiwac.cilentapp.patrobot.bean.BaseEntity;
import android.util.Log;

import com.aiwac.cilentapp.patrobot.bean.BaseEntity;
import com.aiwac.cilentapp.patrobot.bean.User;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.robotapp.commonlibrary.bean.WifiInfo;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.JsonException;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

import static com.aiwac.robotapp.commonlibrary.common.Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE;
import static com.aiwac.robotapp.commonlibrary.common.Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE;


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


    //解析errorCode
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
    //解析ClientID  :帐号在数据库中的id
    public static String parseClientID(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_ID);
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
            String result = root.getString(Constant.RETURN_JSON_ERRORDESC);
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
    public static String baseEntity2JsonString(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
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

    //将BaseEntity对象转换成json
    public static JSONObject baseEntity2Json(BaseEntity baseEntity){
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

    /**
     * 发送mac地址给服务器进行绑定
     * businessType=0001
     * @param macAddress
     * @return 发送的json串
     */
    public static String sendMacAddress(String macAddress){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_BUSSINESS_MACADDRESS_CODE);
            JSONObject root=baseEntity2Json(baseEntity);

            root.put(Constant.ROBOT_MAC_ADDRESS,macAddress);
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS+root.toString());
            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //生成查询讲座视频摘要的json
    public static String videoAbstract2Json(){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座音频摘要的json
    public static String audioAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
    //生成查询讲座音视频详情的json
    public static String aVDetail2Json(String lectureID ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
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

    //解析json 获取讲座  视音频的详细信息
    public static aVDetail parseLectureAVDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                aVDetail  lectureAVDetail = new  aVDetail();

                lectureAVDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
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




}
