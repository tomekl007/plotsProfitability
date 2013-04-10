import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class MainClass {

	//first,end,prof
	//static Deque<Integer> result = new ArrayDeque<>();
	static List<Integer> result = new LinkedList<>();
	
	static CountDownLatch latch ;
	
	public static void main(String[] args) throws InterruptedException {
		
		final int plotsProfitability [] = new int[args.length-1];
		for(int i = 1; i < args.length; i++){
		 plotsProfitability[i-1]= Integer.valueOf(args[i]);
		}
		
		for(Integer i : plotsProfitability)
			System.out.println(i);
		
		int nrOfThreads = Runtime.getRuntime().availableProcessors();
		int expectedProfitability = Integer.parseInt(args[0]);
		latch=new CountDownLatch(nrOfThreads);
		
		boolean oddInterval = false;
		if(plotsProfitability.length%2==0) 
			oddInterval=true; 
		
		final int step = plotsProfitability.length/2; 
		System.out.println(step);
		
		for(int i = 0 ; i < nrOfThreads; i++ ){
			final int i_final = i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int start = i_final*step;
					int end = (i_final+1)*step;
					
					for(int i = 0; i < plotsProfitability.length; i++ ){
						for(int j = start ; j < end; j++){	
							
							if(i<=j){
								int profitability = countProfitability(i,j);
							
								synchronized (result) {
									result.add(i);
									result.add(j);
									result.add(profitability);
								}
							}
						//System.out.println(i +""+ Thread.currentThread().getName()+ " -> " 
							//				+ plotsProfitability[i]);
						}
					}
					latch.countDown();
				}

				private int countProfitability(int i, int j) {
					
					System.out.println("count profitability from : " + i + " to : " + j  );
					int sum=0;
					for(;i<=j;i++)
						sum+=plotsProfitability[i];
					System.out.println(" = "+ sum );
					return sum;
				}
			}).start();
		}
		
		latch.await();
		System.out.println(result);
		
		int index = 1;
		for(Integer i :  result){
			System.out.println( index + " --> " + i);
			index++;
		}
		
	}
	
	

}
