<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="testset1" tilewidth="8" tileheight="8" tilecount="100" columns="10">
 <image source="testset1.png" width="80" height="80"/>
 <tile id="0" probability="0.8"/>
 <tile id="1" probability="0.2"/>
 <tile id="2">
  <animation>
   <frame tileid="2" duration="200"/>
   <frame tileid="3" duration="200"/>
   <frame tileid="4" duration="200"/>
   <frame tileid="3" duration="200"/>
   <frame tileid="2" duration="200"/>
  </animation>
 </tile>
 <tile id="10" probability="0.7">
  <animation>
   <frame tileid="10" duration="400"/>
   <frame tileid="11" duration="400"/>
   <frame tileid="12" duration="400"/>
   <frame tileid="11" duration="400"/>
  </animation>
 </tile>
 <tile id="15">
  <animation>
   <frame tileid="15" duration="350"/>
   <frame tileid="38" duration="350"/>
   <frame tileid="39" duration="350"/>
   <frame tileid="38" duration="350"/>
  </animation>
 </tile>
 <tile id="16">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="3" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="16" duration="270"/>
   <frame tileid="46" duration="270"/>
   <frame tileid="16" duration="270"/>
   <frame tileid="76" duration="270"/>
  </animation>
 </tile>
 <tile id="17">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="17" duration="270"/>
   <frame tileid="47" duration="270"/>
   <frame tileid="17" duration="270"/>
   <frame tileid="77" duration="270"/>
  </animation>
 </tile>
 <tile id="18">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="3" y="3" width="5" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="18" duration="270"/>
   <frame tileid="48" duration="270"/>
   <frame tileid="18" duration="270"/>
   <frame tileid="78" duration="270"/>
  </animation>
 </tile>
 <tile id="19">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="3" width="5" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="19" duration="270"/>
   <frame tileid="49" duration="270"/>
   <frame tileid="19" duration="270"/>
   <frame tileid="79" duration="270"/>
  </animation>
 </tile>
 <tile id="25">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="3" width="8" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="25" duration="270"/>
   <frame tileid="55" duration="270"/>
   <frame tileid="25" duration="270"/>
   <frame tileid="85" duration="270"/>
  </animation>
 </tile>
 <tile id="26">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="3" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
   <object id="2" type="Wall" x="0" y="3" width="3" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="26" duration="270"/>
   <frame tileid="56" duration="270"/>
   <frame tileid="26" duration="270"/>
   <frame tileid="86" duration="270"/>
  </animation>
 </tile>
 <tile id="27">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
   <object id="4" type="Wall" x="5" y="3" width="3" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="27" duration="270"/>
   <frame tileid="57" duration="270"/>
   <frame tileid="27" duration="270"/>
   <frame tileid="87" duration="270"/>
  </animation>
 </tile>
 <tile id="28">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="3" y="0" width="5" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="28" duration="270"/>
   <frame tileid="58" duration="270"/>
   <frame tileid="28" duration="270"/>
   <frame tileid="88" duration="270"/>
  </animation>
 </tile>
 <tile id="29">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="5" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="29" duration="270"/>
   <frame tileid="59" duration="270"/>
   <frame tileid="29" duration="270"/>
   <frame tileid="89" duration="270"/>
  </animation>
 </tile>
 <tile id="35">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="8" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="35" duration="270"/>
   <frame tileid="65" duration="270"/>
   <frame tileid="35" duration="270"/>
   <frame tileid="95" duration="270"/>
  </animation>
 </tile>
 <tile id="36">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="3" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
   <object id="2" type="Wall" x="0" y="0" width="3" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="36" duration="270"/>
   <frame tileid="66" duration="270"/>
   <frame tileid="36" duration="270"/>
   <frame tileid="96" duration="270"/>
  </animation>
 </tile>
 <tile id="37">
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="0" width="5" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
   <object id="2" type="Wall" x="5" y="0" width="3" height="5">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
  <animation>
   <frame tileid="37" duration="270"/>
   <frame tileid="67" duration="270"/>
   <frame tileid="37" duration="270"/>
   <frame tileid="97" duration="270"/>
  </animation>
 </tile>
 <tile id="40" probability="0.1"/>
 <tile id="41" probability="0.1"/>
 <tile id="42" probability="0.1"/>
 <tile id="60">
  <objectgroup draworder="index" id="3">
   <object id="2" type="Wall" x="0" y="0" width="8" height="8">
    <properties>
     <property name="CollisionLayer" type="class" propertytype="BitMask">
      <properties>
       <property name="0" type="bool" value="true"/>
       <property name="1" type="bool" value="true"/>
      </properties>
     </property>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <wangsets>
  <wangset name="path1" type="corner" tile="-1">
   <wangcolor name="dirt" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="grass" color="#00ff00" tile="-1" probability="1"/>
   <wangcolor name="water" color="#0000ff" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="1" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="10" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="11" wangid="0,1,0,1,0,2,0,2"/>
   <wangtile tileid="12" wangid="0,2,0,2,0,1,0,1"/>
   <wangtile tileid="13" wangid="0,2,0,1,0,2,0,2"/>
   <wangtile tileid="14" wangid="0,2,0,2,0,1,0,2"/>
   <wangtile tileid="15" wangid="0,3,0,3,0,3,0,3"/>
   <wangtile tileid="16" wangid="0,3,0,3,0,2,0,2"/>
   <wangtile tileid="17" wangid="0,2,0,2,0,3,0,3"/>
   <wangtile tileid="18" wangid="0,2,0,3,0,2,0,2"/>
   <wangtile tileid="19" wangid="0,2,0,2,0,3,0,2"/>
   <wangtile tileid="20" wangid="0,2,0,1,0,1,0,2"/>
   <wangtile tileid="21" wangid="0,1,0,1,0,1,0,2"/>
   <wangtile tileid="22" wangid="0,2,0,1,0,1,0,1"/>
   <wangtile tileid="23" wangid="0,1,0,2,0,2,0,2"/>
   <wangtile tileid="24" wangid="0,2,0,2,0,2,0,1"/>
   <wangtile tileid="25" wangid="0,2,0,3,0,3,0,2"/>
   <wangtile tileid="26" wangid="0,3,0,3,0,3,0,2"/>
   <wangtile tileid="27" wangid="0,2,0,3,0,3,0,3"/>
   <wangtile tileid="28" wangid="0,3,0,2,0,2,0,2"/>
   <wangtile tileid="29" wangid="0,2,0,2,0,2,0,3"/>
   <wangtile tileid="30" wangid="0,1,0,2,0,2,0,1"/>
   <wangtile tileid="31" wangid="0,1,0,1,0,2,0,1"/>
   <wangtile tileid="32" wangid="0,1,0,2,0,1,0,1"/>
   <wangtile tileid="35" wangid="0,3,0,2,0,2,0,3"/>
   <wangtile tileid="36" wangid="0,3,0,3,0,2,0,3"/>
   <wangtile tileid="37" wangid="0,3,0,2,0,3,0,3"/>
   <wangtile tileid="40" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="41" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="42" wangid="0,1,0,1,0,1,0,1"/>
  </wangset>
 </wangsets>
</tileset>
