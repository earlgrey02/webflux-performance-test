## WebFlux performance test

> **Spring Web MVC와의 비교를 통한 Spring WebFlux 성능 테스트**

본 저장소는 Spring Web MVC와 Spring WebFlux의 성능 비교를 수행하는 테스트에 대한 저장소이다.

## Setup

### Environment

일반적인 개발 환경을 구성하기 위해 대부분의 설정에서 기본값을 사용하도록 했다.
참고로 테스트에 사용된 시스템의 CPU 코어 수는 총 12개이다.

* Spring Web MVC
    * Tomcat
        * 스레드 풀 크기: 200
* Spring WebFlux
    * Reactor Netty
        * 이벤트 루프 스레드 개수: 12(CPU 코어 수)
    * Reactor
        * `ParallelScheduler`의 스레드 풀 크기: 12(CPU 코어 수)
        * `BoundedElasticScheduler`의 스레드 풀 크기: 120(CPU 코어 수 x 10)

### Test Case

Apache JMeter를 사용하여 부하 테스트를 수행하며, 모든 테스트는 Cold Start 환경에서 10,000건의 요청을 한 번에 받도록 하는 방식으로 수행했다.

* CPU Bound
    * 반복문을 통해 총 10,000,000회의 제곱근 연산(`sqrt()`)을 수행
* I/O Bound
    * 데이터베이스로부터 단일 데이터 조회(`SELECT * FROM DEMO WHERE id = 1 LIMIT 1`) 수행

## Result

### CPU Bound

<table>
  <thead>
    <tr>
      <th rowspan="2"></th>
      <th rowspan="2">Spring Web MVC</th>
      <th colspan="3">Spring WebFlux</th>
    </tr> 
    <tr>
      <th>Without Scheduler</th>
      <th>With Scheduler</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">Total Requests</td>
      <td>10000</td>
      <td>10000</td>
      <td>10000</td>
    </tr>
    <tr>
      <td align="center">Average Response Time (ms)</td>
      <td>1435</td>
      <td>2229</td>
      <td>1877</td>
    </tr>
    <tr>
      <td align="center">Min Response Time (ms)</td>
      <td>136</td>
      <td>50</td>
      <td>85</td>
    </tr>
    <tr>
      <td align="center">Max Response Time (ms)</td>
      <td>4421</td>
      <td>8383</td>
      <td>4785</td>
    </tr>
    <tr>
      <td align="center">Response Time Std. Dev.</td>
      <td>787.49</td>
      <td>2824.23</td>
      <td>815.68</td>
    </tr>
    <tr>
      <td align="center">Error Rate (%)</td>
      <td>0.00%</td>
      <td>0.00%</td>
      <td>0.00%</td>
    </tr>
    <tr>
      <td align="center">Throughput (req/sec)</td>
      <td>2201.18</td>
      <td>1022.39</td>
      <td>2085.94</td>
    </tr>
    <tr>
      <td align="center">Peak Thread Count</td>
      <td>222</td>
      <td>46</td>
      <td>57</td>
    </tr>
  </tbody>
</table>

CPU Bound 테스트에서는 **Spring Web MVC가 Spring WebFlux보다 뛰어난 성능**을 보이는 것을 확인할 수 있다.
이는 Spring WebFlux에서는 CPU Bound 작업으로 인해 이벤트 루프 스레드가 차단되면 요청을 더 이상 받을 수 없기 때문이다.
게다가 이벤트 루프 스레드는 기본적으로 CPU 코어 수와 동일한 개수만을 가지는데, 이는 Tomcat의 최대 스레드 수 기본값인 200개에 비해 훨씬 적은 개수이다.
그러므로 Spring WebFlux는 CPU Bound 작업 등의 블로킹 작업에 더 큰 성능 저하를 겪게 된다.

그러나 **Spring WebFlux에서 Reactor의 `ParallelScheduler`를 사용한 경우에는 Spring Web MVC와 비슷한 성능**을 보이는 것을 확인할 수 있다.
이는 CPU Bound 작업이 이벤트 루프와 분리된 전용 스레드 풀에서 처리되기 때문이다.

### I/O Bound

<table>
  <thead>
    <tr>
      <th rowspan="3"></th>
      <th rowspan="3">Spring Web MVC</th>
      <th colspan="3">Spring WebFlux</th>
    </tr> 
    <tr>
      <th rowspan="2">R2DBC</th>
      <th colspan="2">JDBC</th>
    </tr>
    <tr>
      <th>Without Scheduler</th>
      <th>With Scheduler</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">Total Requests</td>
      <td>10000</td>
      <td>10000</td>
      <td>10000</td>
      <td>10000</td>
    </tr>
    <tr>
      <td align="center">Average Response Time (ms)</td>
      <td>2927</td>
      <td>1065</td>
      <td>3225</td>
      <td>3148</td>
    </tr>
    <tr>
      <td align="center">Min Response Time (ms)</td>
      <td>200</td>
      <td>199</td>
      <td>129</td>
      <td>101</td>
    </tr>
    <tr>
      <td align="center">Max Response Time (ms)</td>
      <td>4700</td>
      <td>1585</td>
      <td>7978</td>
      <td>4995</td>
    </tr>
    <tr>
      <td align="center">Response Time Std. Dev.</td>
      <td>1185.45</td>
      <td>289.32</td>
      <td>2363.67</td>
      <td>1206.43</td>
    </tr>
    <tr>
      <td align="center">Error Rate (%)</td>
      <td>0.00%</td>
      <td>0.00%</td>
      <td>0.00%</td>
      <td>0.00%</td>
    </tr>
    <tr>
      <td align="center">Throughput (req/sec)</td>
      <td>1348.25</td>
      <td>2874.39</td>
      <td>1006.40</td>
      <td>1316.70</td>
    </tr>
    <tr>
      <td align="center">Peak Thread Count</td>
      <td>223</td>
      <td>47</td>
      <td>46</td>
      <td>166</td>
    </tr>
  </tbody>
</table>

I/O Bound 테스트에서는 **R2DBC를 사용한 Spring WebFlux가 Spring Web MVC보다 뛰어난 성능**을 보이는 것을 확인할 수 있다.
지연 시간뿐만 아니라 사용한 스레드 수 측면에서도 훨씬 효율적인 결과를 보여주는데, 이는 Spring WebFlux에서는 논블로킹 I/O를 기다리는 동안 이벤트 루프 스레드는 차단되지 않고 계속해서 새로운 요청을 처리할 수 있기
때문이다.

그러나 **Spring WebFlux에서 R2DBC와 같은 논블로킹 I/O가 아닌 JDBC와 같은 블로킹 I/O를 사용할 경우에는 오히려 Spring Web MVC보다 낮은 성능**을 보이는 것을 확인할 수 있다.
이는 앞서 살펴본 CPU Bound 테스트와 마찬가지로, 블로킹 작업이 이벤트 루프 스레드를 점유하면서 전체 처리 성능을 저하시키기 때문이다.
이러한 경우에도 **Reactor의 `BoundedElasticScheduler`를 사용하면 Spring Web MVC와 비슷한 성능**을 낼 수 있음을 확인할 수 있다.

## Summary

테스트 결과를 통해, **Spring WebFlux는 논블로킹 I/O가 많은 환경에서 가장 뛰어난 성능**을 발휘한다는 결론을 도출할 수 있었다.
반면, **CPU Bound 작업이나 블로킹 I/O가 많은 경우에는 Spring Web MVC가 압도적으로 더 나은 성능**을 보였다.
다만, 이러한 상황에서도 **Reactor의 `Scheduler`를 적절히 활용하면 적은 수의 스레드로도 Spring Web MVC에 근접한 성능**을 낼 수 있다는 것으로 결론을 내렸다.
