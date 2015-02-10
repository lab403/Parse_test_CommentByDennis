package com.example.parse_test;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.*;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.os.Build;
import com.parse.ParseACL;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class MainActivity extends Activity
{
	private EditText Text_name,Text_data,Text_id;
	private EditText Text_get1,Text_get2,Text_get3;
	private EditText Text_search;
	
	private Button Button_push,Button_display;
	private Button Button_serach1,Button_serach2;
	private Button Button_update,Button_delete;
	
	private ParseQuery<ParseObject> query;
	
	private TelephonyManager TM;
	private String model,app_ver,imei ;



	private MediaPlayer mPlayer;


    /*
		Dennis ver 1.0.0
        宣告每個物件自己的ID名稱

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);//表示程式開始執行
        setContentView(R.layout.activity_main);
        
        //init for parse//在本機端存放parse資料,之後有網路就可以竟可以同步更新
        Parse.initialize(this, "JDvr3ElOQ3pYXlBqT1twQHx3XvGmpO37XlROoThG", "ISLvJ6xMzRkcpZRi3i3aCNTbAflt9hDl5cERONGP");//parse連接至你的ID
        ParseUser.enableAutomaticUser();//不管有無網路,都可以建立使用者資料
         ParseACL defaultACL = new ParseACL();
        //        
        model = Build.MODEL;//建立一個這可提供存取控制清單，以指示應將物件的讀取或寫入存取權限授與哪個使用者和角色。
        app_ver = getString(R.string.version);
        TM=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = TM.getDeviceId();//在parse拿到不同ID
        
        Text_name =(EditText)findViewById(R.id.text_input1);
        Text_data =(EditText)findViewById(R.id.text_input2);
        Text_id   =(EditText)findViewById(R.id.text_input3);
        
        Text_search=(EditText)findViewById(R.id.text_search);
        
        Text_get1  =(EditText)findViewById(R.id.text_output1);
        Text_get2 =(EditText)findViewById(R.id.text_output2);
        Text_get3 =(EditText)findViewById(R.id.text_output3);
        
        Button_push    =(Button)findViewById(R.id.button_push);
        Button_display =(Button)findViewById(R.id.button_display);
        
        Button_serach1  = (Button)findViewById(R.id.button_search1);
        Button_serach2 = (Button)findViewById(R.id.button_search2);
        
        Button_update = (Button)findViewById(R.id.button_update);
        Button_delete = (Button)findViewById(R.id.button_delete);

        /*
         讓每個物件都能夠在索引資源中找到自己的物件ID
         因為每個物件都有不同的ID名稱

         */

        
        Button_push   .setOnClickListener(push_function);
        Button_serach1.setOnClickListener(search1_function);
        Button_serach2.setOnClickListener(search2_function);
        Button_update .setOnClickListener(update_function);
        Button_delete .setOnClickListener(delete_function);
        Button_display.setOnClickListener(display_function);

        /*
        在每個button上設定一個按鈕按下之後,所發生的事件,,一按下button,就會發生這個按鈕裡的事件
         */
           
       // mPlayer = MediaPlayer.create(this, R.raw.sm23883589);
       // mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
       // mPlayer.setLooping(true);
       // mPlayer.start();
       // mPlayer.setVolume(0.8f, 0.8f);
        
        //mPlayer.pause();
        //mPlayer.stop();
        //mPlayer.release();   
    }

    /**
     * 把每個List<ParseObject>的list傳給DisplayList
     * @param sList  要輸入3個字串
     * 在DisplayList顯示每個list
     * 建立要使用者輸入的新字串,並顯示出來
     */
	private void DisplayList(List<ParseObject> sList)//參數列為ParseObject的List
	{
		String output_string1 = new String("");//把的新類別字串設定為String output_string1
		String output_string2 = new String("");//把的新類別字串設定為String output_string2
		String output_string3 = new String("");//把的新類別字串設定為String output_string3
		for(ParseObject temp : sList )//每個ParseObject的sList
		{
			output_string1 += temp.getString("User_name")+"\n";
			output_string2 += temp.getString("String_1") + "\n";
			output_string3 += temp.getObjectId() + "\n";
		}
		Text_get1.setText(output_string1);//在Text_get1上會顯示string1文字
		Text_get2.setText(output_string2);//在Text_get2上會顯示string2文字
		Text_get3.setText(output_string3);//在Text_get3上會顯示string3文字
		Toast.makeText(this,"Totally get "+sList.size()+" data.", Toast.LENGTH_LONG).show();//在Totally get物件上,會顯示出data,且顯示的時間為4~5秒
	}

    /**
     * 把ParseObject obj傳給DisplayObject
     * @param obj  將得到的User_name和String_1和ObjectId顯示出來
     */

	private void DisplayObject(ParseObject obj)//是把Object的參數丟進DisplayObject,並以條列式顯示出來
	{
		Text_get1.setText(obj.getString("User_name"));//取得字串User_name,並在Text_get1上顯示
		Text_get2.setText(obj.getString("String_1"));//取得字串User_name,並在Text_get2上顯示
		Text_get3.setText(obj.getObjectId());//取得ObjectId,並在Text_get3上顯示
	}


    /**
     * 把TestObject設定給pushObject
     * pushObject所推上去的資料為User_name和String_1和Phone_model和APP_version和User_IMEI
     * 將push上去的資料儲存至pushObject
     * 把pushObject的資料丟給DisplayObject
     * Text_get3會顯示ObjectId
     *
     * 將User_name和String_1和Phone_model和APP_version和User_IMEI的資料推上去,可以顯示從pushObject取得的ID字串
     */
	private Button.OnClickListener push_function = new Button.OnClickListener ()//在push_function的Button.OnClickListener做一操控介面,把一個監聽器建立在button上,按下button所執行的事件
	{
		public void onClick(View v) 
		{
			long startTime = System.currentTimeMillis();//取出目前的時間
			//------------------------------------------------------------------
			if(!Text_name.getText().toString().equals("") && !Text_data.getText().toString().equals(""))  // no null
			//從Text_name內取得字串,並與equals括弧裡的物件是否相等  和Text_data內取得字串,並與equals括弧裡的物件是否相等
            {
				ParseObject pushObject = new ParseObject("TestObject");//把TestObject設定給pushObject
				pushObject.put("User_name", Text_name.getText().toString());//推上去的資料是從Text_name取得的資料
				pushObject.put("String_1", Text_data.getText().toString());//推上去的資料是從Text_data取得的資料
				pushObject.put("Phone_model",model);//推上去的資料是從model取得的資料
				pushObject.put("APP_version",app_ver);//推上去的資料是從app_ver取得的資料
				pushObject.put("User_IMEI",imei);//推上去的資料是從imei取得的資料
				pushObject.saveInBackground();//將push上去的資料儲存至pushObject

				DisplayObject(pushObject);//把pushObject的資料丟給DisplayObject
				Text_get3.setText(pushObject.getObjectId());//在Text_get3上顯示從pushObject取得的ID字串
			}
			//------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理時間
			Toast.makeText(v.getContext(),"Push done for  "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
		}
	
	};

    /*
    建立一個button觸發按鈕的事件
     */

    /**
     * 拿到TestObject設定給query
     * 會判斷query與parse上相同的list是否回報有錯
     * 如果沒有錯誤,就會把資料都給DisplayObject
     *
     * 把sList的資料丟給DisplayObject
     */
	private Button.OnClickListener display_function = new Button.OnClickListener ()
	{
		public void onClick(View v)  
		{//在Button.OnClickListener的 display_function監聽器上,按下button按鈕之後所執行的事件
			long startTime = System.currentTimeMillis();//取出目前的時間
			//----------------------------------------------------------------------------
			query = ParseQuery.getQuery("TestObject");//拿到TestObject設定給query
			//
			query.findInBackground(new FindCallback<ParseObject>()  //�P�u�W�P�B���
					{
						@Override
						public void done(List<ParseObject> sList, ParseException e)   //��Ʒ|�s��list��
						{//在parse的query,抓取parse上的相同的list,e是指回報錯誤
							if (e == null) //e沒有錯誤=null
								DisplayList(sList); //把sList的資料丟給DisplayObject
						}
				
					});
			//------------------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理時間
			Toast.makeText(v.getContext(),"Display done for "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
		}
	};


    /**
     * 會擷取User_name上的全部的使用者名稱
     * 並儲存在query
     * 把sList的所有資料都進DisplayList
     *
     * 把parse上的資料擷取下來,再把sList的所有資料都進DisplayList
     */
	private Button.OnClickListener search1_function = new Button.OnClickListener ()
	{
		public void onClick(View v)
		{//在Button.OnClickListener的search1_function監聽器上,按下button按鈕之後所執行的事件
			long startTime = System.currentTimeMillis();//取出目前的時間
			//-------------------------------------------------------------------------
			query = ParseQuery.getQuery("TestObject");//從ParseQuery中取得Query的TestObject,再把TestObject設定給query
			//request   �Ҥޱ���

			//query.whereEqualTo("User_name", Text_search.getText().toString());
			
			String[] KeyWord = Text_search.getText().toString().split(" ");
			query.whereContainedIn("User_name", Arrays.asList(KeyWord));//擷取User_name上的全部的使用者名稱
			
			//query.whereContains("User_name", Text_search.getText().toString());
						
			//query.whereContainsAll("User_name", Arrays.asList(KeyWord));
			
			query.findInBackground(new FindCallback<ParseObject>()  //�P�u�W�P�B���
					{
						@Override
						public void done(List<ParseObject> sList, ParseException e)   //��Ʒ|�s��list��
						{//在parse的query,抓取parse上的相同的list,e是指回報錯誤
							if (e == null) //e沒有錯誤=null
								DisplayList(sList); //把sList的所有資料都進DisplayList
						}
				
					});
			//-------------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理時間
			Toast.makeText(v.getContext(),"Search1 done for  "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
				
		}
	
	};

    /**
     * 可以利用打搜尋關鍵字,拿到Text_search上的String_1
     * 在parse的query,抓取parse上的相同的list,e是指回報錯誤
     * /e沒有錯誤=null
     * 把sList的資料丟給DisplayObject
     *
     * 可以搜尋parse上的資料
     */
	private Button.OnClickListener search2_function = new Button.OnClickListener ()
	{
		public void onClick(View v)
		{//在Button.OnClickListener的search2_function監聽器上,按下button按鈕之後所執行的事件
			long startTime = System.currentTimeMillis();//取出目前的時間
			//-------------------------------------------------------------------------
			query = ParseQuery.getQuery("TestObject");//從ParseQuery中取得Query的TestObject,再把TestObject設定給query
			//request   �Ҥޱ���
			//query.whereEqualTo("String_1", Text_search.getText().toString());
			
			//String[] KeyWord = Text_search.getText().toString().split(" ");
			//query.whereContainedIn("String_1", Arrays.asList(KeyWord));
			
			query.whereContains("String_1", Text_search.getText().toString());//可以利用打搜尋關鍵字,拿到Text_search上的String_1
			
			//
			query.findInBackground(new FindCallback<ParseObject>()  //�P�u�W�P�B���
					{
						@Override
						public void done(List<ParseObject> sList, ParseException e)   //��Ʒ|�s��list��
						{//在parse的query,抓取parse上的相同的list,e是指回報錯誤
							if (e == null) //e沒有錯誤=null
								DisplayList(sList); //把sList的資料丟給DisplayObject
						}
				
					});
			//-------------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理時間
			Toast.makeText(v.getContext(),"Search2 done for "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
				
		}
	
	};


    /**
     * 可以拿到String_1和Phone_model和APP_version和User_IMEI
     * 並給testObject,推播上去
     *
     * 把String_1和Phone_model和APP_version和User_IMEI推播上去
     */
	private Button.OnClickListener update_function = new Button.OnClickListener ()
	{
		public void onClick(View v)  
		{//在Button.OnClickListener的update_function監聽器上,按下button按鈕之後所執行的事件
			long startTime = System.currentTimeMillis();//取出目前的時間
			//-------------------------------------------------------------------------
			if(!Text_id.getText().toString().equals(""))//從Text_id上所取得的字串是否跟equals括弧裡的字串相同
			{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
				// Retrieve the object by id
				query.getInBackground(Text_id.getText().toString(), new GetCallback<ParseObject>() 
						{
							@Override
							public void done(ParseObject testObject, ParseException e) //e是指回報錯誤
							{
								if (e == null) //e沒有錯誤=null
								{						
									if(!Text_data.getText().toString().equals(""))//如果從Text_data取得字串判斷是否跟equals括弧裡的字串是否相同
									{
										testObject.put("String_1", Text_data.getText().toString());//把從Text_data取得的資料丟給testObject
										testObject.put("Phone_model",model);//把model的資料丟給testObject
										testObject.put("APP_version",app_ver);//把app_ver的資料丟給testObject
										testObject.put("User_IMEI",imei);//把imei的資料丟給testObject
										testObject.saveInBackground();//將testObject上傳至parse
									}
								}
							}
						});
			}
			//-------------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理的時間
			Toast.makeText(v.getContext(),"update done for  "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
		
		}
	};

    /**
     * 建立button刪除鍵
     */
	
	private Button.OnClickListener delete_function = new Button.OnClickListener ()
	{
		public void onClick(View v)  
		{//在Button.OnClickListener的 delete_function監聽器上,按下button按鈕之後所執行的事件
			long startTime = System.currentTimeMillis();//取出目前的時間
			//----------------------------------------------------------------------------
			//ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
			//request   �Ҥޱ���
			//query.whereEqualTo("User_name", Text_name.getText().toString());
			if(query != null)//query不等於沒有回報
			//
			query.findInBackground(new FindCallback<ParseObject>()  //�P�u�W�P�B���
					{
						@Override
						public void done(List<ParseObject> sList, ParseException e)   //��Ʒ|�s��list��
						{
							if (e == null) //e沒有回報錯誤
							{	
								for(int i=0; i<sList.size(); i++)//從parse上的一條條資料逐漸增加
								{
									sList.get(i).deleteInBackground();//刪除parse上的資料
								}
							} 
						}
				
					});
			//------------------------------------------------------------------------------
			long totaltime = System.currentTimeMillis() - startTime;//計算處理的時間
			Toast.makeText(v.getContext(),"Delete done for "+totaltime+"(ms)", Toast.LENGTH_LONG).show();//顯示視窗內容總時間
		}
	};


    /**
     * 宣告播放音樂
     */
	@Override
    protected void onDestroy() {//生命週期
       if(mPlayer != null)//當mPlayer不等於沒有時
    	   mPlayer.release();//撥放
       super.onDestroy();//用super來呼叫onDestroy
    }
	

}
