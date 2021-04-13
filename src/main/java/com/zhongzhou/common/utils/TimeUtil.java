package com.zhongzhou.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	//获取当前时间
	public static String getNowTime(){
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return longSdf.format(new Date());
	}
	public static String getNowTimeFile(){
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyyMMddHHmmss");  
		return longSdf.format(new Date());
	}
	public static String countTimeDifference(Date d1,Date d2){
		long t1=d1.getTime();
		long t2=d2.getTime();
		int t3=(int)((t2-t1)/1000);
		long d=t3/86400;
		long h=t3%86400/3600;
		long m=t3%3600/60;
		long s=t3%60;
		return d+"天"+h+"小时"+m+"分钟"+s+"秒";
	}
	 // 获得本月第一天0点时间  
    public static Date getTimesMonthmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return cal.getTime();  
    }  
  
    // 获得本月最后一天24点时间  
    public static Date getTimesMonthnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        return cal.getTime();  
    }  
  
    public static Date getLastMonthStartMorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getTimesMonthmorning());  
        cal.add(Calendar.MONTH, -1);  
        return cal.getTime();  
    } 
    
    //将long类型转换成字符串（5h8m24s）格式
    public static String formatLongTimeToString(long time){
    	
    	long h=time/3600;
    	long m=time%3600/60;
    	long s=time%60;
    	return h+"h"+m+"m"+s+"s";
    }
	
    public static String getStartTimeByTimes(int flag){
    	Calendar yes=Calendar.getInstance();
    	String starttime;
    	if(flag==1){
    		String s=new SimpleDateFormat("yyyy-MM-dd").format(yes.getTime());
    		starttime=s+" 00:00:00";
    	}else if(flag==2){
    		yes.add(Calendar.DATE, -7);
    		starttime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yes.getTime());
    	}else if(flag==3){
    		yes.add(Calendar.MONTH, -1);
    		starttime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yes.getTime());
    	}else{
    		int year=yes.get(Calendar.YEAR);
    		starttime=year+"-01-01 00:00:00";
    	}
    	return starttime;
    }
    
    public static String getEndTimeByTimes(int flag){
    	Calendar now=Calendar.getInstance();
    	String endtime;
    	if(flag==1){
    		String e=new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
    		endtime=e+" 23:59:59";
    	}else if(flag==2){
    		endtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
    	}else if(flag==3){
    		endtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
    	}else{
    		int year=now.get(Calendar.YEAR);
    		endtime=year+"-12-31 23:59:59";
    	}
    	return endtime;
    }
    

   public static int getDayOfWeek(String s){
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Calendar c=Calendar.getInstance();
	   try {
		   Date d=sdf.parse(s);
		   c.setTime(d);
		   return c.get(Calendar.DAY_OF_WEEK);
	   } catch (ParseException e) {
			e.printStackTrace();
	   }
	   return -1;
	   
   }
    
    
    public static String getAlarmTime(String s,int alarm){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar c=Calendar.getInstance();
    	try {
    		Date d=sdf.parse(s);
    		c.setTime(d);
    		c.add(Calendar.MINUTE, -alarm);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	Date dd=c.getTime();
    	return sdf.format(dd).substring(11);
    }
    
    
    
    //获得活动开始时间
    public static String getEventStarttime(int loopflag,String starttime,String loopday,Date d){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String now=sdf.format(d);
    	String nowday=now.substring(0, 10);
    	int nowofweek=getDayOfWeek(now);
    	
    	String nowstarttime=nowday+" "+starttime;
    	try {
			Date d2=sdf.parse(nowstarttime);
			if(d2.getTime()>d.getTime()){
				//活动暂未开始
				return nowstarttime;
			}else{
				//活动结束
				//获得下一次活动时间
				Calendar c=Calendar.getInstance();
				c.setTime(d2);
				if(loopflag==2){		//每天重复
					c.add(Calendar.DATE, 1);
					return sdf.format(c.getTime());
				}else{			//每周重复
					int loop=Integer.parseInt(loopday);
					int cha=loop+7-nowofweek;
					c.add(Calendar.DATE, cha);
					return sdf.format(c.getTime());
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    //11月27日获得事件开始时间
    public static String getEventStarttimes(String rq,String endrq,String starttime,String loopday,Date d,int allday){
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String now=sdf.format(d);
    	String nowday=now.substring(0, 10);
    	//int nowofweek=getDayOfWeek(now);
    	String[] arr=loopday.split("&");
    	String startrqtime=rq+" "+starttime;
    	String endrqtime=endrq+" "+starttime;
    	try {
			Date ds=sdf.parse(startrqtime);
			Date dn=sdf.parse(endrqtime);
			if(allday==1){
				//全天
				return rq+" "+starttime;   //返回日程开始时间
			}else{
				//非全天
				//获得活动开始时间
				int dow=getDayOfWeek(now);
				//判断活动是否开始
				String todayevent=nowday+" "+starttime;
				Date dd = sdf.parse(todayevent);
				if(loopday.contains(dow+"")){
					//今天有活动
					//查看活动是否已经开始
					//今天活动开始时间
					if(dd.getTime()>=d.getTime()){
						//活动未开始
						return todayevent;
					}else{
						//活动已经开始
						//获得下一次活动的开始时间
						int x=getNextEventOfWeek(arr,dow);
						int y=0;
						if(x<=dow){
							y=x+7-dow;
						}else{
							y=x-dow;
						}
						Calendar c1=Calendar.getInstance();
						c1.setTime(dd);
						c1.add(Calendar.DATE, y);
						if(c1.getTime().getTime()<=dn.getTime() && c1.getTime().getTime()>ds.getTime()){
							return sdf.format(c1.getTime());
						}else{
							//活动结束
							return null;
						}	
					}
					
				}else{
					//获得下次活动时间
					int x=getNextDayOfWeek(arr,dow);
					int y=0;
					if(x<dow){
						y=x+7-dow;
					}else{
						y=x-dow;
					}
					Calendar c1=Calendar.getInstance();
					c1.setTime(dd);
					c1.add(Calendar.DATE, y);
					if(c1.getTime().getTime()<=dn.getTime() && c1.getTime().getTime()>ds.getTime()){
						return sdf.format(c1.getTime());
					}else{
						//活动结束
						return null;
					}	
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	
    	
    	
    	return null;
    }
    
    
    //获取到下一次活动是周几
    public static int getNextEventOfWeek(String[] arr,int nowdayofweek){
    	for(int i=0;i<arr.length;i++){
    		int lp=Integer.parseInt(arr[i]);
    		if(lp==nowdayofweek){
    			if(i<arr.length-1){
    				return Integer.parseInt(arr[i+1]);
    			}else{
    				return Integer.parseInt(arr[0]);
    			}
    		}
    		
    	}
    	return 0;
    }
    
    
    public static int getNextDayOfWeek(String[] arr,int dayofweek){
    	for(int i=0;i<arr.length;i++){
    		if(Integer.parseInt(arr[i])>dayofweek){
    			return Integer.parseInt(arr[i]);
//    			if(i<arr.length-1){
//    				return Integer.parseInt(arr[i+1]);
//    				
//    			}else{
//    				return Integer.parseInt(arr[0]);
//    			}
    		}
    		
    	}
    	return Integer.parseInt(arr[0]);
    }
    
    
    
    public static void main(String[] args){
    	String[] arr=new String[1];
    	arr[0]="6";
    	int x=getNextEventOfWeek(arr,6);
    	System.out.println("返回值=="+x);
    	
    }
    
    
    
}
