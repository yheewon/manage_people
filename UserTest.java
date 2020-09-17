package ksnu.cie.hw;

import java.util.*;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.io.File;

public class UserTest {
	Scanner input=new Scanner(System.in);
	
	private ArrayList<User> ulist=new ArrayList();
	private int now=0;//현재 로그인되어 있는 회원 인덱스 번호 /10=아무것도 아님
	
	public void runUser() throws Exception {
		int choice=0;
		inputData();
		
		while(true) {
			System.out.println("0.로그인   1.신규회원가입   2.회원정보 수정   3.회원탈퇴   4.회원정보 보기   5.종료");
			System.out.print("원하는 번호를 입력하세요: ");
			try {
				if(ulist.size()==0) ulist.add(new Administrator("root","root","관리자","관리자","관리자",1));
				choice=input.nextInt();
				if(choice==0) login();
				else if(choice==1) runMembership();
				else if(choice==2) changeMembership();
				else if(choice==3) removeMembership();
				else if(choice==4) showUser();
				else if(choice==5) break;
			}catch(InputMismatchException e) {
				System.out.println("다시 입력해주세요.");
				input.next();
			}
			outputData();
		}
		System.out.println("종료합니다. ");
	}
	
	public void login() {
		int correct=0;//회원가입되어있는지 알아볼수있는 변수

		if(ulist.size()==0) System.out.println("회원가입부터 해주세요. ");

		else if(ulist.size()!=0) {
			System.out.print("id: ");
			String id=input.next();
			System.out.print("password: ");
			String password=input.next();

			for(int i=0;i<ulist.size();i++) {//첫번째로 동일한 아이디 있는지 확인
				if(id.equals(ulist.get(i).getId())) {
					now=i;
					correct=1;
					//아이디에 맞는 비밀번호 인지 확인
					if(ulist.get(now).getPassword().equals(password)) System.out.println(ulist.get(now).getName()+"님이 로그인하셨습니다.");
					else {
						System.out.println("비밀번호를 확인해주세요.");
						now=10;
					}
				}
			}

			if(correct==0) {//로그인한 아이디가 없음
				now=10;
				System.out.println("id가 맞는지 확인해주세요. ");
			}
		}
	}
	
	public void runMembership() {//회원가입
		String id;
		String password;
		String name;
		String address;
		
		System.out.println("1.일반유저  2.관리자");
		System.out.print("원하는 번호를 입력해주세요: ");
		int choice=input.nextInt();
	
		System.out.print("id:");
		id=input.next();
		System.out.print("password:");
		password=input.next();
		System.out.print("name: ");
		name=input.next();
		System.out.print("address: ");
		address=input.next();
		
		if(choice==1) ulist.add(new GeneralMember(id,password,name,address,"일반유저",0));
		else if(choice==2) ulist.add(new Administrator(id,password,name,address,"관리자",1));
	}
	
	public void changeMembership() {
		if(now==10) System.out.println("로그인해주세요.");
		else if(ulist.get(now).getType()==1) Admi(1);
		else if(ulist.get(now).getType()==0) change(now);
	}
	
	public void change(int num) {//회원정보 수정 함수
		String change;
		
		System.out.println("1.id/pass 수정  2.이름수정  3.주소수정");
		System.out.print("원하는 번호를 입력해주세요: " );	
		int choice=input.nextInt();
		
		if(choice==1) {
			System.out.print("아이디를 수정하세요 : ");
			change=input.next();
			ulist.get(num).setId(change);
			System.out.print("비밀번호를 수정하세요 :");
			change=input.next();
			ulist.get(num).setPassword(change);
		}
		else if(choice==2) {
			System.out.print("이름을 수정하세요 :");
			change=input.next();
			ulist.get(num).setName(change);
		}
		else if(choice==3) {
			System.out.print("주소를 수정하세요 :");
			change=input.next();
			ulist.get(num).setAddress(change);
		}
		
		System.out.println("수정이 완료되었습니다.");
	}
	
	public void Admi(int num) {//관리자용 num=1이면 수정 2면 탈퇴
		int cm_now=0;
		String change;
		
		showUser();
		System.out.println("수정및 탈퇴시키고 싶은 회원의 아이디를 입력하세요: ");
		String st=input.next();
		for(int i=0;i<ulist.size();i++) {
			if(ulist.get(i).getId().equals(st)) cm_now=i;
		}
		if(num==1) change(cm_now);
		else if(num==2) remove(cm_now);
		
	}
	
	public void removeMembership() {
		if(now==10) System.out.println("로그인해주세요.");
		else if(ulist.get(now).getType()==1) Admi(2);
		else if(ulist.get(now).getType()==0) remove(now);
	}
	
	public void remove(int num) {
		System.out.println("탈퇴합니다.");
		ulist.remove(num);
	}
	
	public void outputData() throws Exception {
		DataOutputStream out=new DataOutputStream(new FileOutputStream("C:\\Users\\heewon\\Desktop\\Book_theewon.txt"));
		for(int i=0;i<ulist.size();i++) {
			String en=Encrypt(ulist.get(i).getPassword(),"key");
			out.writeUTF(ulist.get(i).getId());
			out.writeUTF(en);
			out.writeUTF(ulist.get(i).getName());
			out.writeUTF(ulist.get(i).getAddress());
			out.writeUTF(ulist.get(i).getGrade());
			//out.writeUTF("\n");
		}
		out.flush();
		out.close();
	}
	
	public void inputData() throws Exception{
		DataInputStream in = null;
		FileInputStream fis = null;
		try{
			in=new DataInputStream(fis=new FileInputStream("C:\\Users\\heewon\\Desktop\\Book_theewon.txt"));
			while(in.available()>0) {
				String id;
				String password;
				String name;
				String address;
				String grade;
				id=in.readUTF();
				password=in.readUTF();
				String de=Decrypt(password,"key");
				name=in.readUTF();
				address=in.readUTF();
				grade=in.readUTF();

				if("일반유저".equals(grade)) ulist.add(new GeneralMember(id,password,name,address,grade,0));
				else if("관리자".equals(grade)) ulist.add(new Administrator(id,password,name,address,grade,1));
			}
		} 
		catch (IOException e)
		{
			System.out.println("데이터 저장 실패");
		}
		in.close();
		fis.close();
	}

	public void showUser() {
		for(int i=0;i<ulist.size();i++) {
		System.out.println("id: "+ulist.get(i).getId()+" password:      name: "+ulist.get(i).getName()+" address: "+ulist.get(i).getAddress()+" grade: "+ulist.get(i).getGrade());
		}
	}
	
	 public static String Decrypt(String text, String key) throws Exception{
         Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
         byte[] keyBytes=new byte[16];
         byte[] b=key.getBytes("UTF-8");
         int len=b.length;
         if(len>keyBytes.length) len=keyBytes.length;
         System.arraycopy(b, 0, keyBytes, 0, len);
         SecretKeySpec keySpec=new SecretKeySpec(keyBytes,"AES");
         IvParameterSpec ivSpec=new IvParameterSpec(keyBytes);
         cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
         
         Decoder decoder=Base64.getDecoder();
         byte[] results=decoder.decode(text.getBytes());
         return new String(results,"UTF-8");
      }
      public static String Encrypt(String text, String key) throws Exception{
         Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
         byte[] keyBytes=new byte[16];
         byte[] b=key.getBytes("UTF-8");
         int len=b.length;
         if(len>keyBytes.length) len=keyBytes.length;
         System.arraycopy(b, 0, keyBytes, 0, len);
         SecretKeySpec keySpec=new SecretKeySpec(keyBytes,"AES");
         IvParameterSpec ivSpec=new IvParameterSpec(keyBytes);
         cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
         
         Encoder encoder=Base64.getEncoder();
         byte[] results=encoder.encode(text.getBytes());
         return new String(results,"UTF-8");
      }
      
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		UserTest ut=new UserTest();
		ut.runUser();
	}
	
}
