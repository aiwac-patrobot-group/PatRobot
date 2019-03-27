package com.aiwac.cilentapp.patrobot.utils;



import com.aiwac.cilentapp.patrobot.bean.BaseEntity;
import android.util.Log;

import com.aiwac.cilentapp.patrobot.bean.BaseEntity;
import com.aiwac.cilentapp.patrobot.bean.User;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.cilentapp.patrobot.bean.videoAbstractInfo;
import com.aiwac.cilentapp.patrobot.bean.videoInfo;
import com.aiwac.robotapp.commonlibrary.bean.WifiInfo;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.JsonException;
import com.aiwac.robotapp.commonlibrary.utils.ImageUtil;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.json.JSONArray;
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

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static String parseObject(User user, String opt) {
        try{
            JSONObject root = new JSONObject();
            root.put(Constant.JSON_OPT, opt);

            JSONArray jsonArray = new JSONArray();

            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put(Constant.USER_REGISTER_NUMBER, user.getNumber());
            userJson.put("passwd", user.getPassword());
            jsonArray.put(0, userJson);

            root.put(Constant.JSON_OBJECT_USER_NAME, jsonArray);


            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
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


            root.put(Constant.ROBOT_MAC_ADDRESS,"macAddress");//rui添加，原，macAddress
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS+root.toString());


            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    /**
     * 转发指令 视频通话
     * @param uuid
     * @return
     */
    public static String commendVideoChat(String uuid){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_MESSAGE_TRANSFORM_CODE);
            JSONObject root=baseEntity2Json(baseEntity);
            JSONObject data=new JSONObject();
            data.put(Constant.WEBSOCKET_COMMAND_TYPE,Constant.WEBSOCKET_COMMAND_VIDEO_CODE);
            //data.put(Constant.WEBSOCKET_COMMAND_VIDEO_UUID,uuid);
            root.put(Constant.WEBSOCKET_MESSAGE_TRANSFORM,data.toString());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS+root.toString());
            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    /**
     * 结束视频通话的指令转发
     * @return
     */
    public static String commendEndVideoChat(){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_MESSAGE_TRANSFORM_CODE);
            JSONObject root=baseEntity2Json(baseEntity);
            JSONObject data=new JSONObject();
            data.put(Constant.WEBSOCKET_COMMAND_TYPE,Constant.WEBSOCKET_COMMAND_END_VIDEO_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_TRANSFORM,data.toString());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS+root.toString());
            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    /**
     * 结束视频通话的指令转发
     * @return
     */
    public static String commendMoveDirection(String direction){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_MESSAGE_TRANSFORM_CODE);
            JSONObject root=baseEntity2Json(baseEntity);
            JSONObject data=new JSONObject();
            data.put(Constant.WEBSOCKET_COMMAND_TYPE,Constant.WEBSOCKET_COMMAND_MOVE_CODE);
            data.put(Constant.WEBSOCKET_COMMAND_MOVE_DIRECTION,direction);
            root.put(Constant.WEBSOCKET_MESSAGE_TRANSFORM,data.toString());

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
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE,Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE,Constant.WEBSOCKET_MESSAGE_TYPE_Client);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_VIDEO_AUDIO_TYPE, "video");
            root.put(Constant.WEBSOCKET_MESSAGE_PAGE_NUMBER, 1);
            root.put(Constant.WEBSOCKET_MESSAGE_PAGE_SIZE, 10);
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
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE,WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE,Constant.WEBSOCKET_MESSAGE_TYPE_Client);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_VIDEO_AUDIO_TYPE, "audio");
            root.put(Constant.WEBSOCKET_MESSAGE_PAGE_NUMBER, 1);
            root.put(Constant.WEBSOCKET_MESSAGE_PAGE_SIZE, 10);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    // 生成消息转发请求json
    public static String messageTransform2Json(String transformMessage){

        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE,Constant.WEBSOCKET_MESSAGE_TRANSFORM_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE,Constant.WEBSOCKET_MESSAGE_TYPE_Client);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_TRANSFORM,transformMessage);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION,e);

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




//解析json 获取  视频 音频的摘要信息、

    public static videoAbstractInfo parseLectureAVAbstractInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                videoAbstractInfo aVAbstractInfo = new videoAbstractInfo();

                aVAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
                aVAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE));
                aVAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                aVAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));
                JSONArray jsonArray = root.getJSONArray(Constant.WEBSOCKET_MESSAGE_DATA);

                for (int i = 0; i < jsonArray.length(); i++) {
                    videoInfo lectureCourse = new videoInfo();
                    JSONObject lectureCourseJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    lectureCourse.setType(lectureCourseJson.getString(Constant.WEBSOCKET_MESSAGE_VIDEO_AUDIO_TYPE));
                    lectureCourse.setLectureID(lectureCourseJson.getString(Constant.WEBSOCKET_FILE_ID));
                    lectureCourse.setTitle(lectureCourseJson.getString(Constant.WEBSOCKET_FILE_TITLE));
                    lectureCourse.setDescription(lectureCourseJson.getString(Constant.WEBSOCKET_FILE_DESC));
                    lectureCourse.setCover(lectureCourseJson.getString(Constant.WEBSOCKET_FILE_COVER));
                    lectureCourse.setLink(lectureCourseJson.getString(Constant.WEBSOCKET_FILE_URL));
                    aVAbstractInfo.getLectureCourseAbstracts().add(lectureCourse);
                }
                return aVAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }
    // 生成投食转发请求json
    public static String feedTransform2Json(String feedTime[]){

        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE,Constant.WEBSOCKET_MESSAGE_FEEDTRANSFORM_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE,Constant.WEBSOCKET_MESSAGE_TYPE_Client);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEB_SOCKET_TIME_POINTS,feedTime);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION,e);

        }
    }

    // 生成投食转发请求json
    public static String navigateTransform2Json(String NavigateTime[]){

        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE,Constant.WEBSOCKET_MESSAGE_NAVIGATETRANSFORM_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE,Constant.WEBSOCKET_MESSAGE_TYPE_Client);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEB_SOCKET_TIME_POINTS,NavigateTime);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION,e);

        }
    }

}
