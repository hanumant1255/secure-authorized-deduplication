# secure-authorized-deduplication
A Hybrid Cloud Approach for Secure Authorized Deduplication

Data deduplication is one of important data compression techniques for eliminating duplicate copies of repeating data,
and has been widely used in cloud storage to reduce the amount of storage space and save bandwidth. To protect the confidentiality
of sensitive data while supporting deduplication, the convergent encryption technique has been proposed to encrypt the data before
outsourcing. To better protect data security, this paper makes the first attempt to formally address the problem of authorized data
deduplication. Different from traditional deduplication systems, the differential privileges of users are further considered in duplicate
check besides the data itself.We also present several new deduplication constructions supporting authorized duplicate check in a hybrid
cloud architecture. Security analysis demonstrates that our scheme is secure in terms of the definitions specified in the proposed
security model. As a proof of concept, we implement a prototype of our proposed authorized duplicate check scheme and conduct
testbed experiments using our prototype. We show that our proposed authorized duplicate check scheme incurs minimal overhead
compared to normal operations.
