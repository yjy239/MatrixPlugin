package com.yjy.matrixplugin.matrixserver;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/08/02
 *     desc   : 消费者生产者队列，来自EventBus的思想
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventQueue {
    /**
     * 做成一个链式队列，也能做成一个数组的顺序队列
     * 链式队列优势是没有容量限制
     */

    private Event head;
    private Event tail;

    /**
     * 入队
     * @param event
     */
    synchronized void enqueue(Event event){
        if(event == null){
            return;
        }

        //如果头部和尾部为空，两个指向一处
        //如果加到尾部就好出d队伍了
        if(tail == null){
            tail = event;
        } else if(head == null){
            head = tail = event;
        }

    }










}
