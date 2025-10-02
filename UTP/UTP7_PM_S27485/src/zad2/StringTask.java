package zad2;

public class StringTask implements Runnable {
    private final String txt;
    private final int times;
    private String result;
    private TaskState state;
    private boolean isAbort;

    public StringTask(String txt, int times) {
        this.txt = txt;
        this.times = times;
        this.state = TaskState.CREATED;
        this.isAbort = false;
    }
    @Override
    public void run(){
        this.state = TaskState.RUNNING;
        String sb = "";
        for (int i = 0 ; i < times ; i++){
            if (isAbort){
                this.state = TaskState.ABORTED;
                break;
            }
            sb += reverse(txt);
        }
        this.result = sb;
        if (!isAbort){
            this.state = TaskState.READY;
        }
    }
    private String reverse(String s){
        char[] arr = s.toCharArray();
        for ( int i = 0; i< arr.length/2; i++){
            char temp = arr[i];
            arr[arr.length - i - 1] = temp;
        }
        String toString = "";
        for (int i = 0; i< arr.length; i++){
            toString += arr[i];
        }
        return toString;
    }

    public String getResult() {
        return result;
    }

    public TaskState getState() {
        return state;
    }

    public void start(){
        Thread t = new Thread(this);
        t.start();
    }
    public void abort(){
        this.isAbort = true;
    }
    public boolean isDone(){
        return state == TaskState.ABORTED || state == TaskState.READY;
    }

}

