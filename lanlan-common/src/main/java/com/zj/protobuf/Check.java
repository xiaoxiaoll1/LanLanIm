// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: check.proto
package com.zj.protobuf;

public final class Check {
  private Check() {}
  public static void registerAllExtensions(
          com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
          com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
            (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface CheckMsgOrBuilder extends
          // @@protoc_insertion_point(interface_extends:CheckMsg)
          com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *协议版本号。第一版本：1，以此类推。
     * </pre>
     *
     * <code>int32 version = 1;</code>
     * @return The version.
     */
    int getVersion();

    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>string fromUser = 2;</code>
     * @return The fromUser.
     */
    java.lang.String getFromUser();
    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>string fromUser = 2;</code>
     * @return The bytes for fromUser.
     */
    com.google.protobuf.ByteString
    getFromUserBytes();

    /**
     * <code>.CheckMsg.MsgType msgType = 3;</code>
     * @return The enum numeric value on the wire for msgType.
     */
    int getMsgTypeValue();
    /**
     * <code>.CheckMsg.MsgType msgType = 3;</code>
     * @return The msgType.
     */
    Check.CheckMsg.MsgType getMsgType();

    /**
     * <pre>
     *发送时间
     * </pre>
     *
     * <code>int64 createTime = 4;</code>
     * @return The createTime.
     */
    long getCreateTime();
  }
  /**
   * Protobuf type {@code CheckMsg}
   */
  public static final class CheckMsg extends
          com.google.protobuf.GeneratedMessageV3 implements
          // @@protoc_insertion_point(message_implements:CheckMsg)
          CheckMsgOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use CheckMsg.newBuilder() to construct.
    private CheckMsg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private CheckMsg() {
      fromUser_ = "";
      msgType_ = 0;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
            UnusedPrivateParameter unused) {
      return new CheckMsg();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private CheckMsg(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
              com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              version_ = input.readInt32();
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();

              fromUser_ = s;
              break;
            }
            case 24: {
              int rawValue = input.readEnum();

              msgType_ = rawValue;
              break;
            }
            case 32: {

              createTime_ = input.readInt64();
              break;
            }
            default: {
              if (!parseUnknownField(
                      input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
                e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
      return Check.internal_static_CheckMsg_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
      return Check.internal_static_CheckMsg_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                      Check.CheckMsg.class, Check.CheckMsg.Builder.class);
    }

    /**
     * Protobuf enum {@code CheckMsg.MsgType}
     */
    public enum MsgType
            implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>PING = 0;</code>
       */
      PING(0),
      /**
       * <code>PONG = 1;</code>
       */
      PONG(1),
      UNRECOGNIZED(-1),
      ;

      /**
       * <code>PING = 0;</code>
       */
      public static final int PING_VALUE = 0;
      /**
       * <code>PONG = 1;</code>
       */
      public static final int PONG_VALUE = 1;


      public final int getNumber() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalArgumentException(
                  "Can't get the number of an unknown enum value.");
        }
        return value;
      }

      /**
       * @param value The numeric wire value of the corresponding enum entry.
       * @return The enum associated with the given numeric wire value.
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static MsgType valueOf(int value) {
        return forNumber(value);
      }

      /**
       * @param value The numeric wire value of the corresponding enum entry.
       * @return The enum associated with the given numeric wire value.
       */
      public static MsgType forNumber(int value) {
        switch (value) {
          case 0: return PING;
          case 1: return PONG;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<MsgType>
      internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
              MsgType> internalValueMap =
              new com.google.protobuf.Internal.EnumLiteMap<MsgType>() {
                public MsgType findValueByNumber(int number) {
                  return MsgType.forNumber(number);
                }
              };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalStateException(
                  "Can't get the descriptor of an unrecognized enum value.");
        }
        return getDescriptor().getValues().get(ordinal());
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
        return Check.CheckMsg.getDescriptor().getEnumTypes().get(0);
      }

      private static final MsgType[] VALUES = values();

      public static MsgType valueOf(
              com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
                  "EnumValueDescriptor is not for this type.");
        }
        if (desc.getIndex() == -1) {
          return UNRECOGNIZED;
        }
        return VALUES[desc.getIndex()];
      }

      private final int value;

      private MsgType(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:CheckMsg.MsgType)
    }

    public static final int VERSION_FIELD_NUMBER = 1;
    private int version_;
    /**
     * <pre>
     *协议版本号。第一版本：1，以此类推。
     * </pre>
     *
     * <code>int32 version = 1;</code>
     * @return The version.
     */
    @java.lang.Override
    public int getVersion() {
      return version_;
    }

    public static final int FROMUSER_FIELD_NUMBER = 2;
    private volatile java.lang.Object fromUser_;
    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>string fromUser = 2;</code>
     * @return The fromUser.
     */
    @java.lang.Override
    public java.lang.String getFromUser() {
      java.lang.Object ref = fromUser_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs =
                (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fromUser_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>string fromUser = 2;</code>
     * @return The bytes for fromUser.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
    getFromUserBytes() {
      java.lang.Object ref = fromUser_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref);
        fromUser_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int MSGTYPE_FIELD_NUMBER = 3;
    private int msgType_;
    /**
     * <code>.CheckMsg.MsgType msgType = 3;</code>
     * @return The enum numeric value on the wire for msgType.
     */
    @java.lang.Override public int getMsgTypeValue() {
      return msgType_;
    }
    /**
     * <code>.CheckMsg.MsgType msgType = 3;</code>
     * @return The msgType.
     */
    @java.lang.Override public Check.CheckMsg.MsgType getMsgType() {
      @SuppressWarnings("deprecation")
      Check.CheckMsg.MsgType result = Check.CheckMsg.MsgType.valueOf(msgType_);
      return result == null ? Check.CheckMsg.MsgType.UNRECOGNIZED : result;
    }

    public static final int CREATETIME_FIELD_NUMBER = 4;
    private long createTime_;
    /**
     * <pre>
     *发送时间
     * </pre>
     *
     * <code>int64 createTime = 4;</code>
     * @return The createTime.
     */
    @java.lang.Override
    public long getCreateTime() {
      return createTime_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
      if (version_ != 0) {
        output.writeInt32(1, version_);
      }
      if (!getFromUserBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, fromUser_);
      }
      if (msgType_ != Check.CheckMsg.MsgType.PING.getNumber()) {
        output.writeEnum(3, msgType_);
      }
      if (createTime_ != 0L) {
        output.writeInt64(4, createTime_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (version_ != 0) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(1, version_);
      }
      if (!getFromUserBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, fromUser_);
      }
      if (msgType_ != Check.CheckMsg.MsgType.PING.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
                .computeEnumSize(3, msgType_);
      }
      if (createTime_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
                .computeInt64Size(4, createTime_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof Check.CheckMsg)) {
        return super.equals(obj);
      }
      Check.CheckMsg other = (Check.CheckMsg) obj;

      if (getVersion()
              != other.getVersion()) return false;
      if (!getFromUser()
              .equals(other.getFromUser())) return false;
      if (msgType_ != other.msgType_) return false;
      if (getCreateTime()
              != other.getCreateTime()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + VERSION_FIELD_NUMBER;
      hash = (53 * hash) + getVersion();
      hash = (37 * hash) + FROMUSER_FIELD_NUMBER;
      hash = (53 * hash) + getFromUser().hashCode();
      hash = (37 * hash) + MSGTYPE_FIELD_NUMBER;
      hash = (53 * hash) + msgType_;
      hash = (37 * hash) + CREATETIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
              getCreateTime());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static Check.CheckMsg parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Check.CheckMsg parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Check.CheckMsg parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Check.CheckMsg parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Check.CheckMsg parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Check.CheckMsg parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Check.CheckMsg parseFrom(java.io.InputStream input)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseWithIOException(PARSER, input);
    }
    public static Check.CheckMsg parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Check.CheckMsg parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseDelimitedWithIOException(PARSER, input);
    }
    public static Check.CheckMsg parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Check.CheckMsg parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseWithIOException(PARSER, input);
    }
    public static Check.CheckMsg parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
              .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Check.CheckMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
              ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code CheckMsg}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:CheckMsg)
            Check.CheckMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
        return Check.internal_static_CheckMsg_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
        return Check.internal_static_CheckMsg_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        Check.CheckMsg.class, Check.CheckMsg.Builder.class);
      }

      // Construct using Check.CheckMsg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
              com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        version_ = 0;

        fromUser_ = "";

        msgType_ = 0;

        createTime_ = 0L;

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
      getDescriptorForType() {
        return Check.internal_static_CheckMsg_descriptor;
      }

      @java.lang.Override
      public Check.CheckMsg getDefaultInstanceForType() {
        return Check.CheckMsg.getDefaultInstance();
      }

      @java.lang.Override
      public Check.CheckMsg build() {
        Check.CheckMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public Check.CheckMsg buildPartial() {
        Check.CheckMsg result = new Check.CheckMsg(this);
        result.version_ = version_;
        result.fromUser_ = fromUser_;
        result.msgType_ = msgType_;
        result.createTime_ = createTime_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
              com.google.protobuf.Descriptors.FieldDescriptor field,
              java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
              com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
              com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
              com.google.protobuf.Descriptors.FieldDescriptor field,
              int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
              com.google.protobuf.Descriptors.FieldDescriptor field,
              java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Check.CheckMsg) {
          return mergeFrom((Check.CheckMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Check.CheckMsg other) {
        if (other == Check.CheckMsg.getDefaultInstance()) return this;
        if (other.getVersion() != 0) {
          setVersion(other.getVersion());
        }
        if (!other.getFromUser().isEmpty()) {
          fromUser_ = other.fromUser_;
          onChanged();
        }
        if (other.msgType_ != 0) {
          setMsgTypeValue(other.getMsgTypeValue());
        }
        if (other.getCreateTime() != 0L) {
          setCreateTime(other.getCreateTime());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws java.io.IOException {
        Check.CheckMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Check.CheckMsg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int version_ ;
      /**
       * <pre>
       *协议版本号。第一版本：1，以此类推。
       * </pre>
       *
       * <code>int32 version = 1;</code>
       * @return The version.
       */
      @java.lang.Override
      public int getVersion() {
        return version_;
      }
      /**
       * <pre>
       *协议版本号。第一版本：1，以此类推。
       * </pre>
       *
       * <code>int32 version = 1;</code>
       * @param value The version to set.
       * @return This builder for chaining.
       */
      public Builder setVersion(int value) {

        version_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *协议版本号。第一版本：1，以此类推。
       * </pre>
       *
       * <code>int32 version = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearVersion() {

        version_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object fromUser_ = "";
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>string fromUser = 2;</code>
       * @return The fromUser.
       */
      public java.lang.String getFromUser() {
        java.lang.Object ref = fromUser_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
                  (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          fromUser_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>string fromUser = 2;</code>
       * @return The bytes for fromUser.
       */
      public com.google.protobuf.ByteString
      getFromUserBytes() {
        java.lang.Object ref = fromUser_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
                  com.google.protobuf.ByteString.copyFromUtf8(
                          (java.lang.String) ref);
          fromUser_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>string fromUser = 2;</code>
       * @param value The fromUser to set.
       * @return This builder for chaining.
       */
      public Builder setFromUser(
              java.lang.String value) {
        if (value == null) {
          throw new NullPointerException();
        }

        fromUser_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>string fromUser = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearFromUser() {

        fromUser_ = getDefaultInstance().getFromUser();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>string fromUser = 2;</code>
       * @param value The bytes for fromUser to set.
       * @return This builder for chaining.
       */
      public Builder setFromUserBytes(
              com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        checkByteStringIsUtf8(value);

        fromUser_ = value;
        onChanged();
        return this;
      }

      private int msgType_ = 0;
      /**
       * <code>.CheckMsg.MsgType msgType = 3;</code>
       * @return The enum numeric value on the wire for msgType.
       */
      @java.lang.Override public int getMsgTypeValue() {
        return msgType_;
      }
      /**
       * <code>.CheckMsg.MsgType msgType = 3;</code>
       * @param value The enum numeric value on the wire for msgType to set.
       * @return This builder for chaining.
       */
      public Builder setMsgTypeValue(int value) {

        msgType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>.CheckMsg.MsgType msgType = 3;</code>
       * @return The msgType.
       */
      @java.lang.Override
      public Check.CheckMsg.MsgType getMsgType() {
        @SuppressWarnings("deprecation")
        Check.CheckMsg.MsgType result = Check.CheckMsg.MsgType.valueOf(msgType_);
        return result == null ? Check.CheckMsg.MsgType.UNRECOGNIZED : result;
      }
      /**
       * <code>.CheckMsg.MsgType msgType = 3;</code>
       * @param value The msgType to set.
       * @return This builder for chaining.
       */
      public Builder setMsgType(Check.CheckMsg.MsgType value) {
        if (value == null) {
          throw new NullPointerException();
        }

        msgType_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>.CheckMsg.MsgType msgType = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsgType() {

        msgType_ = 0;
        onChanged();
        return this;
      }

      private long createTime_ ;
      /**
       * <pre>
       *发送时间
       * </pre>
       *
       * <code>int64 createTime = 4;</code>
       * @return The createTime.
       */
      @java.lang.Override
      public long getCreateTime() {
        return createTime_;
      }
      /**
       * <pre>
       *发送时间
       * </pre>
       *
       * <code>int64 createTime = 4;</code>
       * @param value The createTime to set.
       * @return This builder for chaining.
       */
      public Builder setCreateTime(long value) {

        createTime_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送时间
       * </pre>
       *
       * <code>int64 createTime = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearCreateTime() {

        createTime_ = 0L;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
              final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
              final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:CheckMsg)
    }

    // @@protoc_insertion_point(class_scope:CheckMsg)
    private static final Check.CheckMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Check.CheckMsg();
    }

    public static Check.CheckMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<CheckMsg>
            PARSER = new com.google.protobuf.AbstractParser<CheckMsg>() {
      @java.lang.Override
      public CheckMsg parsePartialFrom(
              com.google.protobuf.CodedInputStream input,
              com.google.protobuf.ExtensionRegistryLite extensionRegistry)
              throws com.google.protobuf.InvalidProtocolBufferException {
        return new CheckMsg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<CheckMsg> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<CheckMsg> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public Check.CheckMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
          internal_static_CheckMsg_descriptor;
  private static final
  com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internal_static_CheckMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
  getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
          descriptor;
  static {
    java.lang.String[] descriptorData = {
            "\n\013check.proto\"\204\001\n\010CheckMsg\022\017\n\007version\030\001 " +
                    "\001(\005\022\020\n\010fromUser\030\002 \001(\t\022\"\n\007msgType\030\003 \001(\0162\021" +
                    ".CheckMsg.MsgType\022\022\n\ncreateTime\030\004 \001(\003\"\035\n" +
                    "\007MsgType\022\010\n\004PING\020\000\022\010\n\004PONG\020\001B\007B\005Checkb\006p" +
                    "roto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
            .internalBuildGeneratedFileFrom(descriptorData,
                    new com.google.protobuf.Descriptors.FileDescriptor[] {
                    });
    internal_static_CheckMsg_descriptor =
            getDescriptor().getMessageTypes().get(0);
    internal_static_CheckMsg_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_CheckMsg_descriptor,
            new java.lang.String[] { "Version", "FromUser", "MsgType", "CreateTime", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}